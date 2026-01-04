import 'package:postit_frontend/widget/default_appbar.dart';
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
    // 페이지 진입 시 데이터 로드
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final postIdInt = int.tryParse(postId);
      if (postIdInt != null && controller.post.value == null) {
        controller.getDetailPost(postIdInt);
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
                        Obx(() => controller.post.value == null
                            ? const Center(child: CircularProgressIndicator())
                            : Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    controller.post.value?.title ?? "",
                                    style: TextStyle(
                                      color: mainController.isDarkMode.value
                                          ? Colors.white
                                          : const Color(0xFF0e171b),
                                      fontSize: 26,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                  const SizedBox(height: 16),
                                  Text(
                                    controller.post.value?.content ?? "",
                                    style: TextStyle(
                                      color: mainController.isDarkMode.value
                                          ? Colors.grey[300]
                                          : const Color(0xFF0e171b),
                                      fontSize: 16,
                                      height: 1.6,
                                    ),
                                  ),
                                  const SizedBox(height: 12),
                                  Text(
                                    "Posted by ${controller.post.value?.nickname ?? controller.post.value?.username ?? "Unknown"}",
                                    style: TextStyle(
                                      color: mainController.isDarkMode.value
                                          ? Colors.grey[400]
                                          : const Color(0xFF6c757d),
                                      fontSize: 13,
                                    ),
                                  ),
                                ],
                              )),
                        // 수정/삭제 버튼 (작성자만 보이게) - post content 아래
                        Obx(() {
                          // post가 로드되지 않았으면 버튼을 보이지 않음
                          if (controller.post.value == null) {
                            return const SizedBox.shrink();
                          }
                          return controller.isAuthor()
                              ? Padding(
                                  padding: const EdgeInsets.only(
                                      top: 16, bottom: 16),
                                  child: Row(
                                    children: [
                                      TextButton(
                                        onPressed: () {
                                          Get.toNamed('/post',
                                              arguments: controller.post.value);
                                        },
                                        style: TextButton.styleFrom(
                                          foregroundColor:
                                              mainController.isDarkMode.value
                                                  ? Colors.blue[300]
                                                  : Colors.blue,
                                        ),
                                        child: const Text('수정'),
                                      ),
                                      const SizedBox(width: 8),
                                      TextButton(
                                        onPressed: () {
                                          Get.dialog(
                                            AlertDialog(
                                              title: const Text('삭제 확인'),
                                              content:
                                                  const Text('정말 삭제하시겠습니까?'),
                                              actions: [
                                                TextButton(
                                                  onPressed: () => Get.back(),
                                                  child: const Text('취소'),
                                                ),
                                                TextButton(
                                                  onPressed: () {
                                                    Get.back();
                                                    controller.deletePost();
                                                  },
                                                  style: TextButton.styleFrom(
                                                    foregroundColor: Colors.red,
                                                  ),
                                                  child: const Text('삭제'),
                                                ),
                                              ],
                                            ),
                                          );
                                        },
                                        style: TextButton.styleFrom(
                                          foregroundColor: Colors.red,
                                        ),
                                        child: const Text('삭제'),
                                      ),
                                    ],
                                  ),
                                )
                              : const SizedBox.shrink();
                        }),
                        const SizedBox(height: 16),
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
                        // 댓글 작성 TextField
                        Obx(() => Container(
                              padding: const EdgeInsets.all(12),
                              decoration: BoxDecoration(
                                color: mainController.isDarkMode.value
                                    ? const Color(0xFF1E1E1E)
                                    : Colors.grey[100],
                                borderRadius: BorderRadius.circular(8),
                                border: Border.all(
                                  color: mainController.isDarkMode.value
                                      ? Colors.grey[700]!
                                      : Colors.grey[300]!,
                                ),
                              ),
                              child: Row(
                                children: [
                                  Expanded(
                                    child: TextField(
                                      controller: controller.commentController,
                                      style: TextStyle(
                                        color: mainController.isDarkMode.value
                                            ? Colors.white
                                            : const Color(0xFF0e171b),
                                        fontSize: 14,
                                      ),
                                      decoration: InputDecoration(
                                        hintText: "댓글을 입력하세요...",
                                        hintStyle: TextStyle(
                                          color: mainController.isDarkMode.value
                                              ? Colors.grey[500]
                                              : Colors.grey[600],
                                          fontSize: 14,
                                        ),
                                        border: InputBorder.none,
                                        contentPadding:
                                            const EdgeInsets.symmetric(
                                                horizontal: 8),
                                      ),
                                    ),
                                  ),
                                  const SizedBox(width: 8),
                                  TextButton(
                                    onPressed: () {
                                      if (!mainController.isLoggedIn.value) {
                                        Get.snackbar(
                                          '알림',
                                          '로그인 후 이용할 수 있습니다.',
                                          snackPosition: SnackPosition.BOTTOM,
                                          backgroundColor:
                                              mainController.isDarkMode.value
                                                  ? const Color(0xFF1E1E1E)
                                                  : Colors.white,
                                          colorText:
                                              mainController.isDarkMode.value
                                                  ? Colors.white
                                                  : const Color(0xFF0e171b),
                                        );
                                        return;
                                      }
                                      final postIdInt = int.tryParse(postId);
                                      if (postIdInt != null) {
                                        controller.saveComment(postIdInt);
                                      }
                                    },
                                    style: TextButton.styleFrom(
                                      backgroundColor: const Color(0xFF13ec13),
                                      foregroundColor: const Color(0xFF0d1b0d),
                                      padding: const EdgeInsets.symmetric(
                                          horizontal: 16, vertical: 8),
                                      shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(8),
                                      ),
                                    ),
                                    child: const Text(
                                      '작성',
                                      style: TextStyle(
                                        fontSize: 14,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            )),
                        const SizedBox(height: 16),
                        Obx(() => controller.comments.isEmpty
                            ? Padding(
                                padding:
                                    const EdgeInsets.symmetric(vertical: 24.0),
                                child: Center(
                                  child: Text(
                                    "댓글이 없습니다.",
                                    style: TextStyle(
                                      color: mainController.isDarkMode.value
                                          ? Colors.grey[400]
                                          : Colors.grey,
                                      fontSize: 14,
                                    ),
                                  ),
                                ),
                              )
                            : Column(
                                children: controller.comments.map((c) {
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
                                                    c.author,
                                                    style: TextStyle(
                                                        color: mainController
                                                                .isDarkMode
                                                                .value
                                                            ? Colors.white
                                                            : const Color(
                                                                0xFF0e171b),
                                                        fontWeight:
                                                            FontWeight.bold,
                                                        fontSize: 14),
                                                  ),
                                                  const Spacer(),
                                                  // 댓글 삭제 버튼 (작성자만 보이게)
                                                  // isCommentAuthor는 reactive하지 않으므로 Obx 밖에서 체크
                                                  controller.isCommentAuthor(c)
                                                      ? TextButton(
                                                          onPressed: () {
                                                            Get.dialog(
                                                              AlertDialog(
                                                                title:
                                                                    const Text(
                                                                        '댓글 삭제'),
                                                                content: const Text(
                                                                    '정말 삭제하시겠습니까?'),
                                                                actions: [
                                                                  TextButton(
                                                                    onPressed:
                                                                        () => Get
                                                                            .back(),
                                                                    child:
                                                                        const Text(
                                                                            '취소'),
                                                                  ),
                                                                  TextButton(
                                                                    onPressed:
                                                                        () {
                                                                      Get.back();
                                                                      controller
                                                                          .deleteComment(
                                                                              c.id);
                                                                    },
                                                                    style: TextButton
                                                                        .styleFrom(
                                                                      foregroundColor:
                                                                          Colors
                                                                              .red,
                                                                    ),
                                                                    child:
                                                                        const Text(
                                                                            '삭제'),
                                                                  ),
                                                                ],
                                                              ),
                                                            );
                                                          },
                                                          style: TextButton
                                                              .styleFrom(
                                                            padding:
                                                                const EdgeInsets
                                                                    .symmetric(
                                                                    horizontal:
                                                                        8),
                                                            minimumSize:
                                                                Size.zero,
                                                            tapTargetSize:
                                                                MaterialTapTargetSize
                                                                    .shrinkWrap,
                                                          ),
                                                          child: Text(
                                                            '삭제',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize: 12,
                                                            ),
                                                          ),
                                                        )
                                                      : const SizedBox.shrink(),
                                                ],
                                              ),
                                              const SizedBox(height: 4),
                                              Text(
                                                c.content,
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
