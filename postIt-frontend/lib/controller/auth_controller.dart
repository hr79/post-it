import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/service/auth_service.dart';
import 'dart:html' as html;

class AuthController extends GetxController {
  TextEditingController idController = TextEditingController(text: "username1");
  TextEditingController pwController = TextEditingController(text: "1234");

  final AuthService _authService = AuthService();
  final isDarkMode = false.obs;
  RxBool isLoggedIn = false.obs; // 로그인 한 상태인지

  void toggleTheme() {
    isDarkMode.value = !isDarkMode.value;
  }

  setIdController(String inputText) {
    idController = TextEditingController(text: inputText);
    print(idController.text);
  }

  setPwController(String inputText) {
    pwController = TextEditingController(text: inputText);
    print(pwController.text);
  }

  login() async {
    // todo
    // 로그인 실패시 로그인 다이얼로그가 꺼지는게 아니라 입력한 값은 그대로 남아있게+ 다시 로그인하라는 알림
    isLoggedIn.value =
        await _authService.login(idController.text, pwController.text);
    print("isLoggedIn: $isLoggedIn");

    if (isLoggedIn.value == true) {
      final mainController = Get.find<MainController>();
      mainController.checkLoginStatus();
      Get.offAllNamed("/");
    } else {
      Get.snackbar("로그인 실패", "아이디 또는 비밀번호가 일치하지 않습니다.");
    }
  }

  // 로그아웃
  logOut() {
    _authService.logOut();
    isLoggedIn.value = false;
  }

  // 구글 로그인
  getOAuth2Url() async {
    String? googleClientUrl = await _authService.getOAuth2Url();
    print("googleClientUrl: $googleClientUrl");

    if (googleClientUrl != null) {
      _handleOAuthCallback(googleClientUrl);
    }
  }

  void _handleOAuthCallback(String googleClientUrl) {
    print("handleOAuthCallback");
    // 현재 탭에서 열기
    html.window.location.href = googleClientUrl;
  }
}
