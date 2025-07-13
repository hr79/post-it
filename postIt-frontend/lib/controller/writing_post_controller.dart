import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/api/api_client.dart';
import 'package:postit_frontend/app_route.dart';

import '../model/post.dart';

class WritingPostController extends GetxController {
  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();

  final ApiClient _apiClient = ApiClient();
  final Dio _dio = Dio();
  final basedUrl = AppRoute.basedUrl;
  final _storage = GetStorage();

  savePost() async {
    String title = titleController.text;
    String content = contentController.text;
    print(title);
    print(content);
    try {
      var response = await _apiClient.post(
        "$basedUrl/board",
        {"title": title, "content": content},
      );
      print("${response.data}");
    } on DioException catch (e) {
      print(e.message);
      print(e.stackTrace);
    }
  }

  setTitleController(String title) {
    titleController = TextEditingController(text: title);
  }

  setContentController(String content) {
    contentController = TextEditingController(text: content);
  }

  // 게시글 수정
  editPost(Post post) async {
    print(":::: editPost");
    try {
      String? token = _storage.read("token");

      var response = await _dio.patch(
        "$basedUrl/board/${post.id}",
        data: {
          "title": titleController.text,
          "content": contentController.text,
        },
        options: Options(
          headers: {"Authorization": "Bearer $token"},
          contentType: Headers.jsonContentType,
        ),
      );
      print("response : ${response.toString()}");
    } catch (e){
      print(e);
    }
  }
}
