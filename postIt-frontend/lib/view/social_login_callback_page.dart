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
    var fragment = uri.fragment;
    print("fragment : $fragment");
    var queryParam = fragment.contains('?') ? fragment.split('?')[1] : '';
    print("queryParam : $queryParam");
    var accessToken = Uri.splitQueryString(queryParam)['access_token'];
    // var accessToken = fragment['access_token'];
    print("accessToken : $accessToken");

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (accessToken != null) {
        html.window.localStorage['accessToken'] = accessToken;
        controller.saveToken(accessToken);

        // 팝업이면 닫고, 아니면 리디렉션
        final opener = html.window.opener;

        if (opener != null) {
          print("팝업 상태입니다");
          try {
            opener.postMessage({'accessToken': accessToken}, uri.origin);
          } catch (e) {
            print('postMessage 오류: $e');
          }
          html.window.close(); // 팝업이면 자동으로 닫힘
        } else {
          // 새 탭으로 열린 경우: 홈으로 리디렉트
          print("새탭 상태입니다");
          Get.offAllNamed('/');
        }
      } else {
        print("액세스 토큰이 없습니다");
      }
    });

    return Scaffold(
      body: Center(child: Text('로그인 처리 중...')),
    );
  }
}
