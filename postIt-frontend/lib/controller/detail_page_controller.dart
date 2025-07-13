import 'dart:io';

import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/post.dart';

import '../model/comment.dart';

class DetailPageController extends GetxController {
  final Dio _dio = Dio();

  final String basedUrl = AppRoute.basedUrl; // local
  Rxn<Post> post = Rxn();
  RxList<Comment> comments = RxList();
  final _storage = GetStorage();
  TextEditingController commentController = TextEditingController();

  getDetailPost(int id) async {
    print(":::: getDetailPost");

    var resPost = await _dio.get("$basedUrl/board/$id");
    // print(resPost.data["data"]);
    Map<String, dynamic> mapData =
        Map<String, dynamic>.from(resPost.data["data"]);
    post.value = Post.fromMap(mapData);
    // print("post.value : ${post.value}");
    // print("post.value.member.username : ${post.value?.member?.username})}");
    // print(resPost.data["data"]["comments"]);
    List<Map<String, dynamic>> commentListMapData =
        List<Map<String, dynamic>>.from(resPost.data["data"]["comments"]);

    if (commentListMapData.isNotEmpty) {
      List<Comment> commentList =
          commentListMapData.map((c) => Comment.fromMap(c)).toList();
      comments.value = commentList;
      print("${comments.value}");
    }
  }

  // 글 작성자 확인
  bool isAuthor() {
    print(":::: isAuthor");
    try {
      String? token = _storage.read("token");
      if (token == null) {
        print("token is null");
        return false;
      }
      var decode = JWT.decode(token);
      print("jwt username: ${decode.payload["sub"]}");
      print(
          "post username: ${post.value!.member?.username ?? "no member info"}");

      if (decode.payload["sub"] == post.value?.member?.username) {
        return true;
      }
    } catch (e) {
      print(e);
      return false;
    }
    return false;
  }

  // 글 삭제
  deletePost() async {}

  saveComment(int id) async {
    print(":::: saveComment");
    String comment = commentController.text;

    String? token = _storage.read("token");
    var response = await _dio.post(
      "$basedUrl/board/$id/comment",
      data: {
        "content": comment,
        "author": "anonymous",
      },
      options: Options(
          headers: {"Authorization": "Bearer $token"},
          contentType: Headers.jsonContentType),
    );
    print(response.toString());
  }

  setCommentController(String inputText) {
    commentController = TextEditingController(text: inputText);
    print(commentController.text);
  }
}
