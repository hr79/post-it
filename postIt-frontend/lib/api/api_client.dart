import 'package:dio/dio.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';

class ApiClient {
  final Dio _dio = Dio();
  final _storage = GetStorage();
  final basedUrl = AppRoute.basedUrl;

  ApiClient() {
    _dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (options, handler) async {
          final accessToken = _storage.read("token");
          if (accessToken != null) {
            options.headers["Authorization"] = "Bearer $accessToken";
          }
          return handler.next(options);
        },
        onResponse: (response, handler) {
          return handler.next(response);
        },
        onError: (DioException e, handler) async {
          print("::::ApiClient onError");

          if (e.response?.statusCode == 401 || e.response?.statusCode == 403) {
            print(e.response?.statusCode);
            var reissueResp = await _dio.post("$basedUrl/auth/reissue");
            print(reissueResp.data);

            String newAccessToken = reissueResp.data;
            _storage.write("token", newAccessToken);

            // 이전 요청 재시도
            var options = Options(
              method: e.requestOptions.method,
              headers: e.requestOptions.headers,
              responseType: e.requestOptions.responseType,
              contentType: e.requestOptions.contentType,
            );
            options.headers?["Authorization"] = "Bearer $newAccessToken";

            var response = await _dio.request(e.requestOptions.path,
                data: e.requestOptions.data,
                queryParameters: e.requestOptions.queryParameters,
                options: options);

            handler.resolve(response);
          }
        },
      ),
    );
  }

  Future<Response> get(String path) async {
    var resp = await _dio.get(path);
    return resp;
  }

  Future<Response> post(String path, Object data) async {
    String? authToken = _storage.read("token");
    return await _dio.post(
      path,
      data: data,
      options: Options(
        contentType: Headers.jsonContentType,
        headers: {"authorization": "Bearer $authToken"},
      ),
    );
    // return response;
  }
}
