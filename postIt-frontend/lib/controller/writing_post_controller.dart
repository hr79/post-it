import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/api/api_client.dart';
import 'package:postit_frontend/app_route.dart';

class WritingPostController extends GetxController {
  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();

  final ApiClient _apiClient = ApiClient();
  final Dio _dio = Dio();
  final basedUrl = AppRoute.basedUrl;

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
}
