import 'package:dio/dio.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';

class AuthService {
  final Dio _dio = Dio();
  final basedUrl = AppRoute.basedUrl;
  final storage = GetStorage();

  Future<bool> login(String id, String pw) async {
    try {
      print("로그인 시도");
      var resp = await _dio.post("$basedUrl/login",
          options: Options(contentType: Headers.jsonContentType),
          data: {"username": id, "password": pw});

      await _setTokenInStorage(resp);

      print("로그인 성공");
      return true;
    } on DioException catch (e) {
      print(e.response);
      print(e.message);
      print(e.error);

      return false;
    }
  }

  Future<void> _setTokenInStorage(resp) async {
    Map<String, String> map = Map<String, String>.from(resp.data);
    String? value = map["access_token"];
    print(value);

    storage.write("token", value);
    print("토큰 저장 완료: ${storage.read("token")}");
  }

  void logOut() async {
    storage.remove("token");
    print("토큰 삭제 완료");
  }

  // 소셜 로그인
  Future<String?> getOAuth2Url() async {
    print("==== TalkService.getOAuth2Url");
    try {
      var res = await _dio.get("$basedUrl/auth/oauth2-login?login_type=google");
      print(res.data);
      return res.data;
    } on DioException catch (e) {
      print(e.message);
    }
    return null;
  }

  Future<void> fetchData() async {
    try {
      final response = await _dio.get('YOUR_SERVER_URL/data');
      print(response.data);
    } on DioError catch (e) {
      if (e.response?.statusCode == 401) {
        //토큰 갱신 실패로 인한 로그인 페이지 이동 등의 처리
        print("로그인 만료");
      }
      print(e);
    }
  }
}
