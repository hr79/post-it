import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/api/api_client.dart';
import 'package:postit_frontend/app_route.dart';

class SignUpController extends GetxController {
  TextEditingController idController = TextEditingController();
  TextEditingController pwController = TextEditingController();
  TextEditingController pwCheckController = TextEditingController();
  TextEditingController emailController = TextEditingController();
  TextEditingController nicknameController = TextEditingController();

  RxBool isSamePassword = true.obs;

  final _dio = Dio();
  final _apiClient = ApiClient();
  final basedUrl = AppRoute.basedUrl;

  signUp() async {
    try {
      String username = idController.text;
      String password = pwController.text;
      String confirmPassword = pwCheckController.text;
      String email = emailController.text;
      String nickname = nicknameController.text;

      var postReq = await _dio.post(
        "$basedUrl/auth/register",
        data: {
          "username": username,
          "password": password,
          "confirmPassword": confirmPassword,
          "email": email,
          "nickname": nickname,
        },
      );
      print(postReq.data);
      Get.offAndToNamed("/");
      Get.snackbar("회원가입 완료", "로그인 해주세요.", duration: Duration(seconds: 4));
    } on DioException catch (e) {
      // print(e.response);
      print(e.response?.data);
      print(e.message);
      print(e.error);
      Get.snackbar("회원가입 실패", "다시 시도 해주세요.", duration: Duration(seconds: 4));
    }
  }

  void checkPasswordMatch() {
    print(":::: validateConfirmPassword");

    if (pwController.text != pwCheckController.text) {
      isSamePassword.value = false;
    } else if (pwCheckController.text.isEmpty ||
        pwController.text == pwCheckController.text) {
      isSamePassword.value = true;
    }
  }
}
