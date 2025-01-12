import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/api/api_client.dart';
import 'package:postit_frontend/app_route.dart';

class WritingPostController extends GetxController {
  TextEditingController titleController = TextEditingController();
  TextEditingController contentController = TextEditingController();
  final storage = FlutterSecureStorage();
  final ApiClient _apiClient = ApiClient();
  final Dio _dio = Dio();
  // final basedUrl = "http://localhost:8080/api";
  final basedUrl = AppRoute.basedUrl;

  savePost() async {
    String title = titleController.text;
    String content = contentController.text;
    print(title);
    print(content);
    try {
      // var accessToken = await storage.read(key: "token");
      // print(":::: $accessToken");
      // var response = await _dio.post("$basedUrl/board",
      //     options: Options(
      //       // headers: {"Authorization": "Bearer $token"},
      //       headers: {"Authorization": "Bearer ${accessToken}"},
      //       contentType: Headers.jsonContentType,
      //     ),
      //     data: {"title": title, "content": content});

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
