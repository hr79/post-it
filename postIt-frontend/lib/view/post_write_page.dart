import 'package:postit_frontend/widget/default_appbar.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/writing_post_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';

import '../model/post.dart';

class PostWritePage extends GetView<WritingPostController> {
  const PostWritePage({super.key});

  static const String route = "/post";

  @override
  Widget build(BuildContext context) {
    final post = Get.arguments; // 전달된 Post 객체
    final mainController = Get.find<MainController>();

    // 수정 모드일 때 한 번만 초기값 세팅
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (post != null && post is Post) {
        // 이미 설정되어 있지 않은 경우에만 설정
        if (controller.titleController.text != post.title ||
            controller.contentController.text != post.content) {
          controller.titleController.text = post.title ?? "";
          controller.contentController.text = post.content ?? "";
        }
      } else {
        // 새 글 작성 모드일 때는 초기화
        controller.titleController.clear();
        controller.contentController.clear();
      }
    });

    return Obx(() => Scaffold(
          backgroundColor: mainController.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: defaultAppBar(
            isDarkMode: mainController.isDarkMode.value,
            leading: IconButton(
              icon: Icon(
                Icons.close,
                color: mainController.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
              ),
              onPressed: () {
                Get.back();
              },
            ),
            title: Text(
              post != null ? "게시글 수정" : "새 글 작성",
              style: TextStyle(
                color: mainController.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
            actions: [
              TextButton(
                onPressed: () async {
                  if (post != null) {
                    await controller.editPost(post);
                  } else {
                    await controller.savePost();
                  }
                  // 메인페이지로 이동하고 뒤로가기 방지
                  Get.offAllNamed("/");
                },
                child: const Text(
                  "Post",
                  style: TextStyle(color: Colors.blue),
                ),
              ),
            ],
          ),
          body: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              children: [
                // Row(
                //   children: [
                //     CircleAvatar(
                //       // backgroundImage: AssetImage(''), // 사용자 이미지 경로 설정
                //       radius: 24,
                //     ),
                //     SizedBox(width: 10),
                //     Expanded(
                //       child: Text(
                //         "Your name", // 사용자 이름 표시
                //         style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                //       ),
                //     ),
                //   ],
                // ),
                SizedBox(height: 20),
                TextField(
                  controller: controller.titleController,
                  onChanged: (inputText) =>
                      controller.setTitleController(inputText),
                  decoration: InputDecoration(
                    hintText: "Title",
                    border: InputBorder.none,
                    hintStyle: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: mainController.isDarkMode.value
                          ? Colors.grey[400]
                          : null,
                    ),
                  ),
                  style: TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                    color: mainController.isDarkMode.value
                        ? Colors.white
                        : Colors.black,
                  ),
                ),
                Expanded(
                  child: TextField(
                    controller: controller.contentController,
                    onChanged: (inputText) =>
                        controller.setContentController(inputText),
                    decoration: InputDecoration(
                      hintText: "Write something...",
                      border: InputBorder.none,
                      hintStyle: TextStyle(
                        color: mainController.isDarkMode.value
                            ? Colors.grey[400]
                            : null,
                      ),
                    ),
                    maxLines: null,
                    // 여러 줄 입력 가능
                    expands: true,
                    // 남은 공간을 모두 채움
                    style: TextStyle(
                      fontSize: 16,
                      color: mainController.isDarkMode.value
                          ? Colors.white
                          : Colors.black,
                    ),
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    IconButton(
                      icon: Icon(
                        Icons.camera_alt_outlined,
                        color: mainController.isDarkMode.value
                            ? Colors.grey[400]
                            : Colors.black,
                      ),
                      onPressed: () {},
                    ),
                    IconButton(
                      icon: Icon(
                        Icons.video_camera_back_outlined,
                        color: mainController.isDarkMode.value
                            ? Colors.grey[400]
                            : Colors.black,
                      ),
                      onPressed: () {},
                    ),
                    IconButton(
                      icon: Icon(
                        Icons.image_outlined,
                        color: mainController.isDarkMode.value
                            ? Colors.grey[400]
                            : Colors.black,
                      ),
                      onPressed: () {},
                    ),
                    IconButton(
                      icon: Icon(
                        Icons.person_add_alt,
                        color: mainController.isDarkMode.value
                            ? Colors.grey[400]
                            : Colors.black,
                      ),
                      onPressed: () {},
                    ),
                  ],
                ),
              ],
            ),
          ),
        ));
  }
}
