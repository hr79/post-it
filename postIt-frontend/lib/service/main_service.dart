import 'package:dio/dio.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/post.dart';

class MainService extends GetxController {
  final Dio _dio = Dio();
  final storage = GetStorage();

  // final String basedUrl = "http://13.209.85.84:8080/";
  // final String basedUrl = "http://13.209.85.84/api"; // ec2 배포했을떼
  // final String basedUrl = "http://localhost:80/api";  // local with nginx
  // final String basedUrl = "http://localhost:8080/api"; // local
  final String basedUrl = AppRoute.basedUrl; // local

  Future<List<Post>?> getPagingPost(int pageNum) async {
    print("==== TalkService.getPagingPost");
    print("pageNum = $pageNum");
    try {
      var res = await _dio.get("$basedUrl/board?page=$pageNum");

      print("res : ${res.data.toString()}");
      // print(res.data["success"]);
      // print(res.data["status"]);
      // print(res.data["message"]);
      // print(res.data["data"]);
      // print(res.data["data"]);

      var mapList = List<Map<String, dynamic>>.from(res.data["data"]);
      List<Post> postList = mapList.map((p) => Post.fromMap(p)).toList();

      return postList;
    } on DioException catch (e) {
      print(e.message);
    }
    return null;
  }

  Future<bool> login(String id, String pw) async {
    try {
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
    print("토큰 저장 완료");
  }

  void logOut() async {
    storage.remove("token");
    print("토큰 삭제 완료");
  }
}
