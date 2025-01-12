import 'package:dio/dio.dart';

class AuthService {
  final Dio _dio = Dio();

  Future<void> fetchData() async {
    try {
      final response = await _dio.get('YOUR_SERVER_URL/data');
      print(response.data);
    } on DioError catch (e) {
      if(e.response?.statusCode == 401){
        //토큰 갱신 실패로 인한 로그인 페이지 이동 등의 처리
        print("로그인 만료");
      }
      print(e);
    }
  }
}