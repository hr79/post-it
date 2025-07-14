import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/controller/main_controller.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:postit_frontend/view/post_write_page.dart';
import 'package:url_launcher/url_launcher_string.dart';

import '../widget/bullet_in_item_widget.dart';

class MainPage extends GetView<MainController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('POST-IT'),
        actions: [
          Padding(
            padding: const EdgeInsets.only(right: 16.0), // 검색 아이콘 우측 여백
            child: IconButton(
              icon: const Icon(Icons.search),
              onPressed: () {
                // 검색 기능 구현
              },
            ),
          ),
          // Padding( // MY
          //   padding: const EdgeInsets.all(8.0),
          //   child: TextButton(
          //     onPressed: () {
          //       _showUserInfoDialog(context);
          //     },
          //     child: Text('MY', style: TextStyle(color: Colors.black)),
          //   ),
          // ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: PopupMenuButton<String>(
              color: Colors.white,
              // PopupMenuButton 사용
              onSelected: (String result) {
                // 메뉴 아이템 선택 시 처리
                if (result == 'profile') {
                  // 프로필 보기
                } else if (result == 'settings') {
                  // 설정
                }
              },
              itemBuilder: (BuildContext context) => <PopupMenuEntry<String>>[
                const PopupMenuItem<String>(
                  value: 'profile',
                  child: Text('프로필'),
                ),
                const PopupMenuItem<String>(
                  value: 'settings',
                  child: Text('설정'),
                ),
                const PopupMenuItem<String>(
                  value: 'logout',
                  child: Text('로그아웃'),
                ),
              ],
              child: const Text('MY',
                  style: TextStyle(color: Colors.black)), // MY 텍스트
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 16.0), // 로그인 버튼 우측 여백
            child: Obx(
              () => controller.isLoggedIn.value
                  ? TextButton(
                      // ElevatedButton 대신 TextButton 사용
                      onPressed: () {
                        // 로그아웃 기능 구현
                        controller.logOut();
                      },
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.black, // 텍스트 색상 검정색
                        textStyle: const TextStyle(fontSize: 16),
                      ),
                      child: const Text('Log Out'),
                    )
                  : TextButton(
                      // ElevatedButton 대신 TextButton 사용
                      onPressed: () {
                        // 로그인 기능 구현
                        showLoginDialog(context);
                      },
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.black, // 텍스트 색상 검정색
                        textStyle: const TextStyle(fontSize: 16),
                      ),
                      child: const Text('Log in'),
                    ),
            ),
          ),
        ],
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Obx(
          () => controller.postList.isEmpty
              ? Text("No Post")
              : ListView.builder(
                  controller: controller.scrollController,
                  // Column -> ListView로 변경하여 스크롤 가능하게 함
                  itemCount: controller.postList.length +
                      (controller.hasMore.value ? 1 : 0),
                  itemBuilder: (BuildContext context, int index) {
                    if (index < controller.postList.length) {
                      var post = controller.postList[index];
                      return BulletinItem(
                          title: post.title!,
                          viewCount: post.viewCount!,
                          post: post);
                    } else {
                      return const Padding(
                        padding: EdgeInsets.symmetric(vertical: 16.0),
                        child: Center(
                          child: Text("마지막 글입니다."),
                        ),
                      );
                    }
                  },
                ),
        ),
      ),
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(bottom: 8.0),
        child: FloatingActionButton(
          onPressed: () {
            // 글 작성 페이지로 이동하는 로직 구현
            Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (context) =>
                      PostWritePage()), // PostWriteScreen은 글 작성 페이지 위젯
            );
          },
          backgroundColor: Colors.lightBlue[300],
          // 은은한 파란색
          child: const Icon(Icons.edit, color: Colors.white),
          // 흰색 글쓰기 아이콘
          elevation: 2,
          // 약간의 그림자 추가
          shape: CircleBorder(),
        ),
      ),
    );
  }

  void showLoginDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return Dialog(
          backgroundColor: Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8.0),
          ),
          child: Stack(
            // Stack 위젯으로 감싸기
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(24, 48, 24, 24), // 상단 패딩 추가
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: <Widget>[
                      const Text(
                        'Log in to POST-IT',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 24),
                      TextField(
                        controller: controller.idController,
                        onChanged: (inputText) =>
                            controller.setIdController(inputText),
                        decoration: InputDecoration(
                          hintText: 'ID',
                          border: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFFD8D8D8)),
                          ),
                          focusedBorder: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFF007AFF)),
                          ),
                          contentPadding: const EdgeInsets.symmetric(
                              vertical: 12.0, horizontal: 16.0),
                        ),
                      ),
                      const SizedBox(height: 16),
                      TextField(
                        controller: controller.pwController,
                        onChanged: (inputText) =>
                            controller.setPwController(inputText),
                        obscureText: true,
                        decoration: InputDecoration(
                          hintText: 'Password',
                          border: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFFD8D8D8)),
                          ),
                          focusedBorder: const OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xFF007AFF)),
                          ),
                          contentPadding: const EdgeInsets.symmetric(
                              vertical: 12.0, horizontal: 16.0),
                        ),
                      ),
                      const SizedBox(height: 8),
                      Align(
                        alignment: Alignment.centerLeft,
                        child: InkWell(
                          onTap: () {
                            launchUrlString(
                                "https://www.nextdoor.com/forgot_password/");
                          },
                          child: const Text(
                            'Forgot your password?',
                            style: TextStyle(
                                color: Color(0xFF007AFF), fontSize: 14),
                          ),
                        ),
                      ),
                      const SizedBox(height: 24),
                      ElevatedButton(
                        // login button
                        onPressed: () {
                          controller.login();
                          Navigator.of(context).pop();
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFF007AFF),
                          foregroundColor: Colors.white,
                          minimumSize: const Size(double.infinity, 48),
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8.0),
                          ),
                        ),
                        child: const Text('Log in',
                            style: TextStyle(fontSize: 16)),
                      ),
                      const SizedBox(height: 16),
                      TextButton(
                        onPressed: () {
                          Get.toNamed("/signup");
                          // launchUrlString("https://www.nextdoor.com/register/");
                        },
                        child: const Text('Sign up',
                            style: TextStyle(fontSize: 16)),
                      ),
                      const SizedBox(height: 24),
                      Column(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: [
                          ElevatedButton(
                            onPressed: () {
                              controller.getOAuth2Url();
                            },
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.white,
                              foregroundColor: Colors.black,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(8.0),
                                side:
                                    const BorderSide(color: Color(0xFFD8D8D8)),
                              ),
                              minimumSize: const Size(140, 48),
                              // fixedSize: Size(200, 40)
                            ),
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: const <Widget>[
                                FaIcon(FontAwesomeIcons.google,
                                    color: Colors.red),
                                SizedBox(width: 8),
                                Text('Google'),
                              ],
                            ),
                          ),
                          Container(
                            height: 10,
                          ),
                          ElevatedButton(
                            onPressed: () {},
                            style: ElevatedButton.styleFrom(
                              backgroundColor: const Color(0xFFFEE500),
                              foregroundColor: Colors.black,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(8.0),
                              ),
                              minimumSize: const Size(140, 48),
                              // fixedSize: Size(180, 40)
                            ),
                            child: SizedBox(
                              width: double.infinity,
                              child: Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Image(
                                    alignment: Alignment.center,
                                    image: AssetImage('assets/kakao_logo.png'),
                                    height: 40,
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
              Positioned(
                // 'X' 버튼 배치
                top: 8,
                right: 8,
                child: IconButton(
                  icon: const Icon(Icons.close),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  void _showUserInfoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('사용자 정보'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('이름: 홍길동'),
              Text('이메일: hong@example.com'),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
              },
              child: Text('닫기'),
            ),
          ],
        );
      },
    );
// ... (main 함수는 이전 코드와 동일)
  }
}
