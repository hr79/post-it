import 'dart:html' as html;

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/social_login_callback_controller.dart';

class SocialLoginCallbackPage extends GetView<SocialLoginCallbackController> {
  const SocialLoginCallbackPage({super.key});

  @override
  Widget build(BuildContext context) {
    print(":::: SocialLoginCallbackPage");
    final uri = Uri.base;
    final accessToken = uri.queryParameters['access_token'];

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (accessToken != null) {
        html.window.localStorage['accessToken'] = accessToken;
        controller.saveToken(accessToken);

        // 팝업이면 닫고, 아니면 리디렉션
        if (html.window.opener != null) {
          print("팝업 상태입니다");
          html.window.opener!
              .postMessage({'accessToken': accessToken}, uri.origin);
          html.window.close(); // 팝업이면 자동으로 닫힘
        } else {
          // 새 탭으로 열린 경우: 홈으로 리디렉트
          print("새탭 상태입니다");

          WidgetsBinding.instance.addPostFrameCallback((_) {
            Navigator.pushReplacementNamed(context, '/');
          });
        }
      }
    });

    return Scaffold(
      body: Center(child: Text('로그인 처리 중...')),
    );
  }
}
