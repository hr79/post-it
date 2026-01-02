import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/detail_page_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';

class PostDetailPage extends GetView<DetailPageController> {
  const PostDetailPage({super.key, required this.postId});

  static const String route = "/detail";

  final String postId;
  final String postTitle = "How to make a perfect salad";
  final String postContent =
      "I have been making salads for myself for years and I think I have perfected the art of making a perfect salad. Here are my tips: 1. Make sure you have a good mix of greens. I usually use spinach and arugula. 2. Add some crunch. This can be anything from nuts to seeds to croutons. 3. Add some protein. This can be chicken, tofu or even hard boiled eggs. 4. Add some fruit. I like to add berries, oranges, or apples. 5. Dressing. You can buy your own dressing or make your own. My favorite is balsamic vinaigrette.";

  @override
  Widget build(BuildContext context) {
    final mainController = Get.find<MainController>();
    return Obx(() => Scaffold(
          backgroundColor: mainController.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: AppBar(
            backgroundColor: mainController.isDarkMode.value
                ? const Color(0xFF1E1E1E)
                : Colors.white,
            elevation: 0,
            leading: IconButton(
              icon: Icon(
                Icons.arrow_back,
                color: mainController.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
              ),
              onPressed: () => Get.back(),
            ),
            title: Text(
              'POST-IT',
              style: TextStyle(
                color: mainController.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
            centerTitle: true,
          ),
          body: LayoutBuilder(
            builder: (context, constraints) {
              final isWide = constraints.maxWidth > 800;
              return SingleChildScrollView(
                padding: EdgeInsets.symmetric(
                  horizontal: isWide ? 80 : 20,
                  vertical: 24,
                ),
                child: Center(
                  child: ConstrainedBox(
                    constraints: const BoxConstraints(maxWidth: 960),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Home / Post",
                          style: TextStyle(
                            color: mainController.isDarkMode.value
                                ? Colors.grey[400]
                                : const Color(0xFF6c757d),
                            fontSize: 14,
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        const SizedBox(height: 16),
                        Obx(() => Text(
                              controller.postTitle.value,
                              style: TextStyle(
                                color: mainController.isDarkMode.value
                                    ? Colors.white
                                    : const Color(0xFF0e171b),
                                fontSize: 26,
                                fontWeight: FontWeight.bold,
                              ),
                            )),
                        const SizedBox(height: 16),
                        Obx(() => Text(
                              controller.postContent.value,
                              style: TextStyle(
                                color: mainController.isDarkMode.value
                                    ? Colors.grey[300]
                                    : const Color(0xFF0e171b),
                                fontSize: 16,
                                height: 1.6,
                              ),
                            )),
                        const SizedBox(height: 12),
                        Obx(() => Text(
                              "Posted by ${controller.postAuthor.value}",
                              style: TextStyle(
                                color: mainController.isDarkMode.value
                                    ? Colors.grey[400]
                                    : const Color(0xFF6c757d),
                                fontSize: 13,
                              ),
                            )),
                        const SizedBox(height: 32),
                        Text(
                          "Comments",
                          style: TextStyle(
                              color: mainController.isDarkMode.value
                                  ? Colors.white
                                  : const Color(0xFF0e171b),
                              fontSize: 20,
                              fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 16),
                        Obx(() => Column(
                              children: controller.commentList.map((c) {
                                return Container(
                                  margin: const EdgeInsets.only(bottom: 20),
                                  child: Row(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      const CircleAvatar(
                                        radius: 20,
                                        backgroundImage: NetworkImage(
                                            "https://lh3.googleusercontent.com/aida-public/AB6AXuCSvLtObV-SOSW0nYAUZQzaWgn-qEECPop_Yfdx-50d6tirGZkec51I6XFrl238YK6ow5nXPf-2yXhXcPwrvze5vGFk_zzjia1hYU-AR0Cr_KvNLPHbx1AjiA50-Vb_GIE0N0gZzAN40-kiGuKFzOf7XBBgxUHZwXH89BuDPwLQyduI_d3RmM56o5ARdjBi1om5GiCZor_dhvVTz6GaNytujGvzmfpIgdtHwtjwqW76ugUyByYvsAbF923TYAoV2v0i1VzrGCbOL4g"),
                                      ),
                                      const SizedBox(width: 12),
                                      Expanded(
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Row(
                                              children: [
                                                Text(
                                                  c['name']!,
                                                  style: TextStyle(
                                                      color: mainController
                                                              .isDarkMode.value
                                                          ? Colors.white
                                                          : const Color(
                                                              0xFF0e171b),
                                                      fontWeight:
                                                          FontWeight.bold,
                                                      fontSize: 14),
                                                ),
                                                const SizedBox(width: 8),
                                                Text(
                                                  c['time']!,
                                                  style: TextStyle(
                                                      color: mainController
                                                              .isDarkMode.value
                                                          ? Colors.grey[400]
                                                          : const Color(
                                                              0xFF6c757d),
                                                      fontSize: 12),
                                                ),
                                              ],
                                            ),
                                            const SizedBox(height: 4),
                                            Text(
                                              c['content']!,
                                              style: TextStyle(
                                                color: mainController
                                                        .isDarkMode.value
                                                    ? Colors.grey[300]
                                                    : const Color(0xFF0e171b),
                                                fontSize: 14,
                                                height: 1.5,
                                              ),
                                            )
                                          ],
                                        ),
                                      )
                                    ],
                                  ),
                                );
                              }).toList(),
                            )),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ));
  }
}
