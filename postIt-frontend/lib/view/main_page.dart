import 'package:postit_frontend/widget/default_appbar.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/view/post_write_page.dart';
import 'package:postit_frontend/widget/login_logout_button.dart';
import 'package:postit_frontend/widget/post_item_widget.dart';

import '../controller/main_controller.dart';
import '../controller/auth_controller.dart';

class MainPage extends GetView<MainController> {
  const MainPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Obx(() => Scaffold(
          backgroundColor: controller.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: defaultAppBar(
            isDarkMode: controller.isDarkMode.value,
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
                onPressed: () {},
              ),
              Padding(
                padding: const EdgeInsets.only(right: 16.0),
                child: Obx(
                  () => LoginLogoutButton(
                    isLoggedIn: controller.isLoggedIn.value,
                    onLoginPressed: () {
                      Get.toNamed("/login");
                    },
                    onLogoutPressed: () {
                      final authController = Get.find<AuthController>();
                      authController.logOut();
                      controller.checkLoginStatus();
                    },
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
                if (!controller.isLoggedIn.value) {
                  Get.snackbar(
                    '알림',
                    '로그인 후 이용할 수 있습니다.',
                    snackPosition: SnackPosition.TOP,
                    backgroundColor: controller.isDarkMode.value
                        ? const Color(0xFF1E1E1E)
                        : Colors.white,
                    colorText: controller.isDarkMode.value
                        ? Colors.white
                        : const Color(0xFF0e171b),
                    margin: const EdgeInsets.only(
                      top: 75,
                      left: 16,
                      right: 16,
                      bottom: 16,
                    ),
                  );
                  return;
                }
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
