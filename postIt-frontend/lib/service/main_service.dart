import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/post.dart';

class MainService extends GetxController {
  final Dio _dio = Dio();

  final String basedUrl = AppRoute.basedUrl; // local

  Future<List<Post>?> getPagingPost(int pageNum) async {
    print("==== MainService.getPagingPost");
    print("pageNum = $pageNum");
    print(basedUrl);
    try {
      var res = await _dio.get("$basedUrl/board?page=$pageNum");

      if (kDebugMode) {
        print("res : ${res.data.toString()}");
        print(res.data["success"]);
        print(res.data["status"]);
        print(res.data["message"]);
        print(res.data["data"]);
      }
      var mapList = List<Map<String, dynamic>>.from(res.data["data"]);
      List<Post> postList = mapList.map((p) => Post.fromMap(p)).toList();

      return postList;
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
