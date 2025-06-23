import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/view/post_detail_page.dart';

import '../model/post.dart';

class BulletinItem extends StatelessWidget {
  const BulletinItem({super.key, required this.title, required this.viewCount, required this.post});

  final Post post;
  final String title;
  final int viewCount;

  @override
  Widget build(BuildContext context) {
    // print("post: ${post.id}");
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          GestureDetector(
            onTap: () {
              Get.toNamed("/board/${post.id}");
            },
            child: Text(title),
          ),
          Text(viewCount.toString()),
        ],
      ),
    );
  }
}
