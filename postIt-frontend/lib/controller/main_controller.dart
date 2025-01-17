import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/model/post.dart';
import 'package:postit_frontend/service/main_service.dart';

class MainController extends GetxController {
  final MainService _mainService = MainService();
  TextEditingController idController = TextEditingController(text: "username1");
  TextEditingController pwController = TextEditingController(text: "1234");
  RxList<Post> postList = <Post>[].obs;
  RxInt pageNum = 0.obs;
  RxBool isLoggedIn = false.obs; // 로그인 한 상태인지
  final _storage = FlutterSecureStorage();

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
  }

  logOut() {
    _mainService.logOut();
    isLoggedIn.value = false;
  }

  @override
  void onInit() async {
    super.onInit();
    print(":::: MainController.onInit");
    await checkLoginStatus();
    getPostlist();
  }

  Future<void> checkLoginStatus() async {
    String? authToken = await _storage.read(key: "token");
    print("authtoken = $authToken");

    if (authToken == null) {
      isLoggedIn.value = false;

      return;
    }
    if (authToken != null && !_isTokenAlive(authToken)) {
      isLoggedIn.value = false;
      await _storage.delete(key: "token");
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
