import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:go_router/go_router.dart';
import 'package:postit_frontend/controller/detail_page_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/controller/sign_up_controller.dart';
import 'package:postit_frontend/controller/writing_post_controller.dart';
import 'package:postit_frontend/view/main_page.dart';
import 'package:postit_frontend/view/post_detail_page.dart';
import 'package:postit_frontend/view/sign_up_page.dart';


void main() {
  final dio = Dio();

  runApp(MyApp(dio: dio)); // myappdp dio 전달
}

class MyApp extends StatelessWidget {
  final Dio dio;

  const MyApp({super.key, required this.dio});

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'POST-IT',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        fontFamily: 'Roboto',
      ),
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
      ],
      initialRoute: "/",
      initialBinding: BindingsBuilder(() {
        Get.lazyPut(() => MainController(), fenix: true);
        Get.lazyPut(() => DetailPageController(), fenix: true);
        Get.lazyPut(() => WritingPostController(), fenix: true);
        Get.lazyPut(() => SignUpController(), fenix: true);
      }),
      home: MainPage(),
    );
  }
}

