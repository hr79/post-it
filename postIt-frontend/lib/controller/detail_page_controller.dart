import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:dio/dio.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/app_route.dart';
import 'package:postit_frontend/model/post.dart';
import 'package:postit_frontend/service/detail_page_service.dart';

import '../model/comment.dart';
import 'main_controller.dart';

class DetailPageController extends GetxController {
  final Dio _dio = Dio();
  final _storage = GetStorage();
  final DetailPageService _detailPageService = DetailPageService();

  final String basedUrl = AppRoute.basedUrl; // local
  Rxn<Post> post = Rxn();

  RxList<Comment> comments = RxList();

  TextEditingController commentController = TextEditingController();

  //ux 개선 임시 데이터
  final postTitle = "Exploring the Art of Urban Gardening".obs;
  final postContent = """
Urban gardening is more than just a hobby; it's a way to connect with nature in the midst of city life. 
From balconies to rooftops, transforming small spaces into green havens is both rewarding and sustainable.
"""
      .obs;
  final postAuthor = "Sarah Miller".obs;

  final commentList = [
    {
      "name": "Alex Johnson",
      "time": "2d",
      "content":
          "This is so inspiring! I've always wanted to start a small garden on my balcony but wasn't sure where to begin."
    },
    {
      "name": "Emily Carter",
      "time": "3d",
      "content":
          "I've been urban gardening for a few years now, and it's truly a game-changer. The fresh herbs and vegetables are a delight."
    },
    {
      "name": "David Lee",
      "time": "4d",
      "content":
          "Great post! I'm curious about vertical gardening. Do you have any recommendations for plants that thrive in vertical setups?"
    },
    {
      "name": "Olivia Brown",
      "time": "5d",
      "content":
          "I love the idea of urban gardening! It's amazing how much you can grow in a small space."
    },
  ].obs;

  getPost(int postId) async {
    print(":::: getPost");
    post.value = await _detailPageService.getPost(postId);
  }

  getComments(int postId) async {
    if (kDebugMode) {
      print("getComments");
    }
    List<Comment>? commentList =
        await _detailPageService.getCommentsInPost(postId);

    comments.clear();

    if (commentList != null) {
      comments.value = commentList;
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
      print("post username: ${post.value?.username ?? "no member info"}");

      if (decode.payload["sub"] == post.value?.username) {
        return true;
      }
    } catch (e) {
      print(e);
      return false;
    }
    return false;
  }

  // 글 삭제
  deletePost() async {
    print(":::: deletePost");
    try {
      String? token = _storage.read("token");
      if (token == null) {
        print("token is null");
        return;
      }
      var response = await _dio.delete("$basedUrl/board/${post.value!.id}",
          options: Options(
            headers: {"Authorization": "Bearer $token"},
          ));

      print(response.toString());

      // 메인 컨트롤러의 포스트 리스트 새로고침
      final mainController = Get.find<MainController>();
      mainController.postList.clear();
      mainController.pageNum.value = 0;
      await mainController.getPostlist();

      // 메인 페이지로 이동
      Get.offAllNamed("/");
    } catch (e) {
      print(e);
    }
  }

  saveComment(int id) async {
    print(":::: saveComment");
    String comment = commentController.text.trim();

    if (comment.isEmpty) {
      return;
    }

    String? token = _storage.read("token");
    try {
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

      // 댓글 작성 성공 시 리스트에 추가
      if (response.data != null && response.data["data"] != null) {
        Map<String, dynamic> commentData =
            Map<String, dynamic>.from(response.data["data"]);
        Comment newComment = Comment.fromMap(commentData);
        // 새로운 리스트를 생성하여 할당 (GetX reactive 업데이트)
        final updatedComments = List<Comment>.from(comments)..add(newComment);
        comments.value = updatedComments;
        commentController.clear();
      }
    } catch (e) {
      print("댓글 작성 실패: $e");
    }
  }

  setCommentController(String inputText) {
    commentController.text = inputText;
    print(commentController.text);
  }

  // 현재 로그인한 사용자 확인
  String? getCurrentUsername() {
    try {
      String? token = _storage.read("token");
      if (token == null) {
        return null;
      }
      var decode = JWT.decode(token);
      return decode.payload["sub"] as String?;
    } catch (e) {
      print(e);
      return null;
    }
  }

  // 댓글 작성자 확인
  bool isCommentAuthor(Comment comment) {
    String? currentUsername = getCurrentUsername();
    if (currentUsername == null) {
      return false;
    }
    return comment.username == currentUsername;
  }

  deleteComment(int commentId) async {
    print(":::: deleteComment");
    try {
      String? token = _storage.read("token");
      if (token == null) {
        print("token is null");
        return;
      }
      var response = await _dio.delete(
        "$basedUrl/board/${post.value!.id}/comment/$commentId",
        options: Options(
          headers: {"Authorization": "Bearer $token"},
        ),
      );
      print(response.toString());

      // 댓글 삭제 성공 시 리스트에서 제거 (GetX reactive 업데이트)
      final updatedComments =
          comments.where((comment) => comment.id != commentId).toList();
      comments.value = updatedComments;
    } catch (e) {
      print("댓글 삭제 실패: $e");
    }
  }
}
