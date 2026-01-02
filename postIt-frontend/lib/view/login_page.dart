import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/widget/oauth_login_button.dart';
import 'package:postit_frontend/widget/auth_text_field.dart';

class LoginPage extends GetView<MainController> {
  const LoginPage({super.key});

  @override
  Widget build(BuildContext context) {
    final idController = TextEditingController();
    final passwordController = TextEditingController();

    return Obx(() => Scaffold(
          backgroundColor: controller.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: AppBar(
            backgroundColor: controller.isDarkMode.value
                ? const Color(0xFF1E1E1E)
                : Colors.white,
            elevation: 0,
            leading: IconButton(
              icon: Icon(
                Icons.arrow_back,
                color: controller.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
              ),
              onPressed: () => Get.back(),
            ),
            title: Text(
              'Login',
              style: TextStyle(
                color: controller.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
            centerTitle: true,
          ),
          body: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(24.0),
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 500),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    const SizedBox(height: 40),
                    // ID 입력창
                    AuthTextField(
                      labelText: 'ID',
                      textEditingController: idController,
                      isDarkMode: controller.isDarkMode.value,
                    ),
                    const SizedBox(height: 16),
                    // Password 입력창
                    AuthTextField(
                      labelText: 'Password',
                      obscureText: true,
                      textEditingController: passwordController,
                      isDarkMode: controller.isDarkMode.value,
                    ),
                    const SizedBox(height: 32),
                    // 로그인 버튼
                    SizedBox(
                      height: 48,
                      child: ElevatedButton(
                        onPressed: () {
                          controller.idController.text = idController.text;
                          controller.pwController.text =
                              passwordController.text;
                          controller.login();
                          if (controller.isLoggedIn.value) {
                            Get.back();
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF13ec13),
                          foregroundColor: const Color(0xFF0d1b0d),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                          ),
                          elevation: 0,
                        ),
                        child: const Text(
                          'Login',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            letterSpacing: 0.015,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 16),
                    // 회원가입 버튼
                    SizedBox(
                      height: 48,
                      child: OutlinedButton(
                        onPressed: () {
                          Get.toNamed('/signup');
                        },
                        style: OutlinedButton.styleFrom(
                          foregroundColor: controller.isDarkMode.value
                              ? Colors.white
                              : const Color(0xFF0e171b),
                          side: BorderSide(
                            color: controller.isDarkMode.value
                                ? Colors.grey[700]!
                                : Colors.grey[300]!,
                          ),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                          ),
                        ),
                        child: const Text(
                          'Sign Up',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(height: 40),
                    // 구분선
                    Row(
                      children: [
                        Expanded(
                          child: Divider(
                            color: controller.isDarkMode.value
                                ? Colors.grey[700]
                                : Colors.grey[300],
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 16),
                          child: Text(
                            'or',
                            style: TextStyle(
                              color: controller.isDarkMode.value
                                  ? Colors.grey[400]
                                  : const Color(0xFF6c757d),
                              fontSize: 14,
                            ),
                          ),
                        ),
                        Expanded(
                          child: Divider(
                            color: controller.isDarkMode.value
                                ? Colors.grey[700]
                                : Colors.grey[300],
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 40),
                    // Google 로그인 버튼
                    OauthLoginButton(
                      onPressed: controller.getOAuth2Url,
                      icon: FontAwesomeIcons.google,
                      text: 'Google Login',
                      backgroundColor: const Color(0xFF4285F4),
                      textColor: Colors.white,
                      iconColor: Colors.white,
                    ),
                    const SizedBox(height: 12),
                    // Kakao 로그인 버튼
                    OauthLoginButton(
                      onPressed: null,
                      icon: FontAwesomeIcons.comment,
                      text: 'Kakao Login',
                      backgroundColor: const Color(0xFFFEE500),
                      textColor: Colors.black,
                      iconColor: Colors.black,
                    ),
                    const SizedBox(height: 12),
                    // Naver 로그인 버튼
                    OauthLoginButton(
                      onPressed: null,
                      icon: FontAwesomeIcons.n,
                      text: 'Naver Login',
                      backgroundColor: const Color(0xFF03C75A),
                      textColor: Colors.white,
                      iconColor: Colors.white,
                    ),
                  ],
                ),
              ),
            ),
          ),
        ));
  }
}
