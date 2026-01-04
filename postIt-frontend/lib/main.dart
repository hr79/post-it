import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/controller/auth_controller.dart';
import 'package:postit_frontend/controller/detail_page_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/controller/sign_up_controller.dart';
import 'package:postit_frontend/controller/social_login_callback_controller.dart';
import 'package:postit_frontend/controller/writing_post_controller.dart';
import 'package:postit_frontend/view/main_page.dart';
import 'package:postit_frontend/view/post_detail_page.dart';
import 'package:postit_frontend/view/post_write_page.dart';
import 'package:postit_frontend/view/sign_up_page.dart';
import 'package:postit_frontend/view/login_page.dart';
import 'package:postit_frontend/view/social_login_callback_page.dart';

void main() async {
  final dio = Dio();

  final uri = Uri.base;
  print("🌐 Uri.base: ${uri.toString()}");
  await GetStorage.init();

  runApp(MyApp(dio: dio, initialRoute: uri.path)); // myappdp dio 전달
}

class MyApp extends StatelessWidget {
  final Dio dio;
  final String initialRoute;

  const MyApp({super.key, required this.dio, required this.initialRoute});

  @override
  Widget build(BuildContext context) {
    // MainController를 먼저 초기화
    Get.put(MainController(), permanent: true);

    return GetBuilder<MainController>(
      builder: (controller) {
        return GetMaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'POST-IT',
          themeMode:
              controller.isDarkMode.value ? ThemeMode.dark : ThemeMode.light,
          theme: ThemeData(
            brightness: Brightness.light,
            scaffoldBackgroundColor: Colors.white,
            colorScheme: ColorScheme.light(
              primary: Colors.blue,
              secondary: Colors.blueAccent,
              surface: Colors.white,
            ),
            appBarTheme: const AppBarTheme(
              backgroundColor: Colors.white,
              foregroundColor: Color(0xFF0e171b),
              elevation: 0,
            ),
          ),
          darkTheme: ThemeData(
            brightness: Brightness.dark,
            scaffoldBackgroundColor: const Color(0xFF121212),
            colorScheme: const ColorScheme.dark(
              primary: Colors.blue,
              secondary: Colors.blueAccent,
              surface: Color(0xFF1E1E1E),
            ),
            appBarTheme: const AppBarTheme(
              backgroundColor: Color(0xFF1E1E1E),
              foregroundColor: Colors.white,
              elevation: 0,
            ),
          ),
          initialRoute: initialRoute,
          getPages: [
            GetPage(name: "/", page: () => MainPage()),
            GetPage(
                name: "/board/:id",
                page: () {
                  final postId = Get.parameters["id"];
                  return PostDetailPage(postId: postId!);
                }
                // binding: BindingsBuilder(() =>
                //     Get.lazyPut<DetailPageController>(() => DetailPageController())),
                ),
            GetPage(name: "/login", page: () => LoginPage()),
            GetPage(name: "/signup", page: () => SignUpPage()),
            GetPage(
                name: "/auth/social/callback",
                page: () => SocialLoginCallbackPage()),
            GetPage(name: '/post', page: () => PostWritePage()),
          ],
          // initialRoute: "/",
          initialBinding: BindingsBuilder(() {
            Get.lazyPut(() => MainController(), fenix: true);
            Get.lazyPut(() => DetailPageController(), fenix: true);
            Get.lazyPut(() => WritingPostController(), fenix: true);
            Get.lazyPut(() => SignUpController(), fenix: true);
            Get.lazyPut(() => SocialLoginCallbackController(), fenix: true);
            Get.lazyPut(() => AuthController(), fenix: true);
          }),
          home: MainPage(),
        );
      },
    );
  }
}
