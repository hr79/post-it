import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/sign_up_controller.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:postit_frontend/widget/auth_text_field.dart';

class SignUpPage extends GetView<SignUpController> {
  const SignUpPage({super.key});

  @override
  Widget build(BuildContext context) {
    final mainController = Get.find<MainController>();
    return Obx(() => Scaffold(
          backgroundColor: mainController.isDarkMode.value
              ? const Color(0xFF121212)
              : Colors.white,
          appBar: AppBar(
            title: Text(
              'Sign Up',
              style: TextStyle(
                color: mainController.isDarkMode.value
                    ? Colors.white
                    : const Color(0xFF0e171b),
                fontWeight: FontWeight.bold,
                fontSize: 18,
              ),
            ),
            backgroundColor: mainController.isDarkMode.value
                ? const Color(0xFF1E1E1E)
                : Colors.white,
            elevation: 0,
          ),
          body: Center(
            child: SingleChildScrollView(
              padding: const EdgeInsets.all(24.0),
              child: ConstrainedBox(
                constraints: const BoxConstraints(maxWidth: 500),
                child: Obx(
                  () => Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      AuthTextField(
                        labelText: "ID",
                        icon: Icons.person,
                        textEditingController: controller.idController,
                        isDarkMode: mainController.isDarkMode.value,
                      ),
                      const SizedBox(height: 16),
                      AuthTextField(
                        labelText: "Password",
                        icon: Icons.lock,
                        obscureText: true,
                        textEditingController: controller.pwController,
                        isDarkMode: mainController.isDarkMode.value,
                      ),
                      const SizedBox(height: 16),
                      AuthTextField(
                        labelText: "Password Check",
                        icon: Icons.check_circle,
                        obscureText: true,
                        textEditingController: controller.pwCheckController,
                        onChanged: (inputText) {
                          controller.checkPasswordMatch();
                        },
                        isSamePassword: controller.isSamePassword.value,
                        errorText: "비밀번호를 확인해주세요.",
                        isDarkMode: mainController.isDarkMode.value,
                      ),
                      const SizedBox(height: 16),
                      AuthTextField(
                        labelText: "Email",
                        icon: Icons.email,
                        textEditingController: controller.emailController,
                        isDarkMode: mainController.isDarkMode.value,
                      ),
                      const SizedBox(height: 16),
                      AuthTextField(
                        labelText: "Nickname",
                        icon: Icons.face,
                        textEditingController: controller.nicknameController,
                        isDarkMode: mainController.isDarkMode.value,
                      ),
                      const SizedBox(height: 32),
                      SizedBox(
                        height: 48,
                        child: ElevatedButton(
                          onPressed: () {
                            // Sign Up logic
                            controller.signUp();
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.blue,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(8.0),
                            ),
                          ),
                          child: const Text(
                            'Sign Up',
                            style: TextStyle(
                              fontSize: 16,
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ));
  }
}
