import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/model/post.dart';
import 'package:postit_frontend/service/main_service.dart';
import 'dart:html' as html;

class MainController extends GetxController {
  final MainService _mainService = MainService();
  TextEditingController idController = TextEditingController(text: "username1");
  TextEditingController pwController = TextEditingController(text: "1234");
  RxList<Post> postList = <Post>[].obs;
  RxInt pageNum = 0.obs;
  RxBool isLoggedIn = false.obs; // 로그인 한 상태인지
  final _storage = GetStorage();

  getPostlist() async {
    List<Post>? pagingPost = await _mainService.getPagingPost(pageNum.value);
    postList.addAll(pagingPost!);
    pageNum.value++;
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
        await _mainService.login(idController.text, pwController.text);
    print("isLoggedIn: $isLoggedIn");
  }

  logOut() {
    _mainService.logOut();
    isLoggedIn.value = false;
  }

  @override
  void onInit() {
    super.onInit();
    print(":::: MainController.onInit");
    getPostlist();
  }


  @override
  void onReady() {
    print(":::: MainController.onReady");
    checkLoginStatus();
  }

  getOAuth2Url() async {
    String? googleClientUrl = await _mainService.getOAuth2Url();
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

  void checkLoginStatus() {
    String? token = _storage.read("token");
    print("token = $token");

    if (token == null) {
      isLoggedIn.value = false;

      return;
    }
    if (!_isTokenAlive(token)) {
      isLoggedIn.value = false;
      _storage.remove("token");
      print("token deleted!");

      return;
    } else {
      isLoggedIn.value = true;

      return;
    }
  }

  bool _isTokenAlive(String? authToken) {
    print(":::: isTokenAlive");
    var decode = JWT.decode(authToken!);
    var expiry = decode.payload["exp"] as int;
    var expiryDate =
        DateTime.fromMillisecondsSinceEpoch(expiry * 1000, isUtc: true);
    print("expiryDate: $expiryDate");

    if (DateTime.now().isAfter(expiryDate)) {
      print("Token is expired");
      return false;
    } else {
      print("Token is alive");
      return true;
    }
  }
}
