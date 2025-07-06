import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/detail_page_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/controller/sign_up_controller.dart';
import 'package:postit_frontend/controller/social_login_callback_controller.dart';
import 'package:postit_frontend/controller/writing_post_controller.dart';
import 'package:postit_frontend/view/main_page.dart';
import 'package:postit_frontend/view/post_detail_page.dart';
import 'package:postit_frontend/view/sign_up_page.dart';
import 'package:postit_frontend/view/social_login_callback_page.dart';


void main() {
  final dio = Dio();

  final uri = Uri.base;
  print("🌐 Uri.base: ${uri.toString()}");

  runApp(MyApp(dio: dio, initialRoute: uri.path)); // myappdp dio 전달
}

class MyApp extends StatelessWidget {
  final Dio dio;
  final String initialRoute;
  const MyApp({super.key, required this.dio, required this.initialRoute});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'POST-IT',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        fontFamily: 'Roboto',
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
        GetPage(name: "/signup", page: () => SignUpPage()),
        GetPage(name: "/auth/social/callback", page: () => SocialLoginCallbackPage())
      ],
      // initialRoute: "/",
      initialBinding: BindingsBuilder(() {
        Get.lazyPut(() => MainController(), fenix: true);
        Get.lazyPut(() => DetailPageController(), fenix: true);
        Get.lazyPut(() => WritingPostController(), fenix: true);
        Get.lazyPut(() => SignUpController(), fenix: true);
        Get.lazyPut(() => SocialLoginCallbackController(), fenix: true);
      }),
      home: MainPage(),
    );
  }
}

