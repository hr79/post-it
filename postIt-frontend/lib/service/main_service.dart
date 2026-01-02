import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/post.dart';

class MainService extends GetxController {
  final Dio _dio = Dio();
  final storage = GetStorage();

  final String basedUrl = AppRoute.basedUrl; // local

  Future<List<Post>?> getPagingPost(int pageNum) async {
    print("==== MainService.getPagingPost");
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

  Future<List<Post>?> fetchPostsFromBackend(int pageNum, int pageSize) async {
    if (kDebugMode) {
      print(":::::: MainService.fetchPostsFromBackend");
      print("pageNum = $pageNum");
    }
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
}
