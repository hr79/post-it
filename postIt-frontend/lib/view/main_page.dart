import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/view/post_write_page.dart';
import 'package:postit_frontend/widget/post_item_widget.dart';

import '../controller/main_controller.dart';

class MainPage extends GetView<MainController> {
  const MainPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Obx(() => Scaffold(
          backgroundColor: controller.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: AppBar(
            backgroundColor: controller.isDarkMode.value
                ? const Color(0xFF1E1E1E)
                : Colors.white,
            elevation: 0,
            centerTitle: true,
            title: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(
                  Icons.hexagon_outlined,
                  color: controller.isDarkMode.value
                      ? Colors.white
                      : const Color(0xFF0e171b),
                ),
                const SizedBox(width: 8),
                Text(
                  'POST-IT',
                  style: TextStyle(
                    color: controller.isDarkMode.value
                        ? Colors.white
                        : const Color(0xFF0e171b),
                    fontWeight: FontWeight.bold,
                    fontSize: 18,
                  ),
                ),
              ],
            ),
            actions: [
              Obx(() => IconButton(
                    icon: Icon(
                      controller.isDarkMode.value
                          ? Icons.light_mode
                          : Icons.dark_mode,
                      color: Theme.of(context).brightness == Brightness.dark
                          ? Colors.white
                          : const Color(0xFF0e171b),
                    ),
                    onPressed: () {
                      controller.toggleTheme();
                    },
                  )),
              IconButton(
                icon: Icon(
                  Icons.search,
                  color: Theme.of(context).brightness == Brightness.dark
                      ? Colors.white
                      : const Color(0xFF0e171b),
                ),
                onPressed: () {
                  // 검색 기능
                },
              ),
              Padding(
                padding: const EdgeInsets.only(right: 16.0),
                child: Obx(
                  () => controller.isLoggedIn.value
                      ? TextButton(
                          onPressed: controller.logOut,
                          style: TextButton.styleFrom(
                            foregroundColor: controller.isDarkMode.value
                                ? Colors.white
                                : Colors.black87,
                            textStyle: const TextStyle(fontSize: 16),
                          ),
                          child: const Text('Log Out'),
                        )
                      : Container(
                          height: 40,
                          constraints: const BoxConstraints(
                            minWidth: 84,
                            maxWidth: 480,
                          ),
                          child: ElevatedButton(
                            onPressed: () {
                              Get.toNamed('/login');
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xFF13ec13),
                              foregroundColor: const Color(0xFF0d1b0d),
                              padding:
                                  const EdgeInsets.symmetric(horizontal: 16),
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(8),
                              ),
                              elevation: 0,
                            ),
                            child: const Text(
                              'Login',
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.bold,
                                letterSpacing: 0.015,
                              ),
                            ),
                          ),
                        ),
                ),
              ),
            ],
          ),
          body: LayoutBuilder(
            builder: (context, constraints) {
              double maxWidth =
                  constraints.maxWidth > 800 ? 800 : constraints.maxWidth;
              return Center(
                child: Container(
                  width: maxWidth,
                  padding:
                      const EdgeInsets.symmetric(horizontal: 16, vertical: 20),
                  child: Obx(
                    () => controller.postList.isEmpty
                        ? Center(
                            child: Text(
                              "No Post",
                              style: TextStyle(
                                color: controller.isDarkMode.value
                                    ? Colors.grey[400]
                                    : Colors.grey,
                                fontSize: 16,
                                fontWeight: FontWeight.w500,
                              ),
                            ),
                          )
                        : ListView.builder(
                            controller: controller.scrollController,
                            itemCount: controller.postList.length +
                                (controller.hasMore.value ? 1 : 0),
                            itemBuilder: (context, index) {
                              if (index < controller.postList.length) {
                                var post = controller.postList[index];
                                return Obx(() => PostItemWidget(
                                      post: post,
                                      isDarkMode: controller.isDarkMode.value,
                                    ));
                              } else {
                                return const Padding(
                                  padding: EdgeInsets.symmetric(vertical: 24.0),
                                  child: Center(
                                    child: Text(
                                      "마지막 글입니다.",
                                      style: TextStyle(
                                        color: Colors.grey,
                                        fontSize: 14,
                                      ),
                                    ),
                                  ),
                                );
                              }
                            },
                          ),
                  ),
                ),
              );
            },
          ),
          floatingActionButton: Padding(
            padding: const EdgeInsets.only(bottom: 8.0, right: 4),
            child: FloatingActionButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => PostWritePage()),
                );
              },
              backgroundColor: const Color(0xFF13ec13),
              elevation: 3,
              shape: const CircleBorder(),
              child: const Icon(Icons.edit, color: Color(0xFF0d1b0d)),
            ),
          ),
        ));
  }
}
