import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/writing_post_controller.dart';

class PostWritePage extends GetView<WritingPostController> {
  const PostWritePage({super.key});

  static const String route = "/post";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "Start post",
          style: TextStyle(color: Colors.black),
        ),
        centerTitle: true,
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.close, color: Colors.black),
          onPressed: () {
            Get.back();
          },
        ),
        actions: [
          TextButton(
            onPressed: () {
              print(controller.titleController.text);
              print(controller.contentController.text);
              controller.savePost();
              Navigator.of(context).pop();
            },
            child: Text(
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
                hintStyle: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            Expanded(
              child: TextField(
                controller: controller.contentController,
                onChanged: (inputText) =>
                    controller.setContentController(inputText),
                decoration: InputDecoration(
                  hintText: "Write something...",
                  border: InputBorder.none,
                ),
                maxLines: null,
                // 여러 줄 입력 가능
                expands: true,
                // 남은 공간을 모두 채움
                style: TextStyle(fontSize: 16),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                IconButton(
                  icon: Icon(Icons.camera_alt_outlined),
                  onPressed: () {},
                ),
                IconButton(
                  icon: Icon(Icons.video_camera_back_outlined),
                  onPressed: () {},
                ),
                IconButton(
                  icon: Icon(Icons.image_outlined),
                  onPressed: () {},
                ),
                IconButton(
                  icon: Icon(Icons.person_add_alt),
                  onPressed: () {},
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
