import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/sign_up_controller.dart';
import 'package:postit_frontend/widget/sign_up_text_field.dart';

class SignUpPage extends GetView<SignUpController> {
  const SignUpPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sign Up'),
        backgroundColor: Colors.white, // AppBar 배경색 흰색으로 변경
        elevation: 0, // AppBar 그림자 제거
      ),
      body: Padding(
        padding: const EdgeInsets.all(20.0), // 여백 조정
        child: Obx(
          () => Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              SignUpTextField(
                labelText: "ID",
                icon: Icons.person,
                textEditingController: controller.idController,
              ),
              const SizedBox(height: 16),
              SignUpTextField(
                labelText: "Password",
                icon: Icons.lock,
                obscureText: true,
                textEditingController: controller.pwController,
              ),
              const SizedBox(height: 16),
              SignUpTextField(
                labelText: "Password Check",
                icon: Icons.check_circle,
                obscureText: true,
                textEditingController: controller.pwCheckController,
                onChanged: (inputText) {
                  controller.checkPasswordMatch();
                },
                isSamePassword: controller.isSamePassword.value,
                errorText: "비밀번호를 확인해주세요.",
              ),
              const SizedBox(height: 16),
              SignUpTextField(
                labelText: "Email",
                icon: Icons.email,
                textEditingController: controller.emailController,
              ),
              const SizedBox(height: 16),
              SignUpTextField(
                labelText: "Nickname",
                icon: Icons.face,
                textEditingController: controller.nicknameController,
              ),
              const SizedBox(height: 32),
              ElevatedButton(
                onPressed: () {
                  // Sign Up logic
                  controller.signUp();
                },
                style: ElevatedButton.styleFrom(
                  // 버튼 스타일 변경
                  backgroundColor: Colors.blue, // 배경색
                  padding: const EdgeInsets.symmetric(vertical: 16.0), // 패딩
                  shape: RoundedRectangleBorder(
                    // 둥근 모양
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                ),
                child: const Text('Sign Up',
                    style: TextStyle(
                        fontSize: 16,
                        color: Colors.white,
                        fontWeight: FontWeight.bold)), // 텍스트 스타일 변경
              ),
            ],
          ),
        ),
      ),
    );
  }
}
