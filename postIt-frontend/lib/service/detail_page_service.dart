import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/comment.dart';
import 'package:postit_frontend/model/post.dart';

class DetailPageService extends GetxController {
  final Dio _dio = Dio();
  final String basedUrl = AppRoute.basedUrl;

  Future<Post?> getPost(int postId) async {
    try {
      if (kDebugMode) {
        print(":::service.getPost");
      }
      var resPost = await _dio.get("$basedUrl/board/$postId");
      if (kDebugMode) {
        print("post data: ${resPost.data["data"]}");
      }
      Map<String, dynamic> mapData =
          Map<String, dynamic>.from(resPost.data["data"]);
      if (mapData.isEmpty) {
        return null;
      }
      return Post.fromMap(mapData);
    } catch (e) {
      print("글 가져오기 실패");
      print(e);
    }
  }

  Future<List<Comment>?> getCommentsInPost(int postId) async {
    try {
      var response = await _dio.get("$basedUrl/board/$postId/comment");

      if (kDebugMode) {
        print("resp: $response");
        print("data: ${response.data["data"]}");
      }
      List<Map<String, dynamic>> commentMapDataList =
          List<Map<String, dynamic>>.from(response.data["data"]);

      if (commentMapDataList.isEmpty) {
        return null;
      }
      return commentMapDataList.map((c) => Comment.fromMap(c)).toList();
    } catch (e) {
      debugPrint(e.toString());
    }
  }
}
