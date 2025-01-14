import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:postit_frontend/model/post.dart';
import 'package:postit_frontend/service/main_service.dart';

class MainController extends GetxController {
  final MainService _mainService = MainService();
  TextEditingController idController = TextEditingController(text: "username1");
  TextEditingController pwController = TextEditingController(text: "password");
  RxList<Post> postList = <Post>[].obs;
  RxInt pageNum = 0.obs;
  RxBool isLoggedIn = false.obs; // 로그인 한 상태인지
  final _storage = FlutterSecureStorage();

  getPostlist() async {
    List<Post>? pagingPost = await _mainService.getPagingPost(pageNum.value);
    // print("pagingPost: $pagingPost");
    postList.addAll(pagingPost!);
    // print(postList[0].title);
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

  login() {
    //todo
    // 비밀번호가 맞지 않아 로그인 실패시에도 isLoggedIn이 true가 되면서 로그인 상테가 됨.
    try {
      _mainService.login(idController.text, pwController.text);
      isLoggedIn.value = true;
    } on DioException catch (e) {
      print(e.message);
      print(e.response);
      print(e.stackTrace);
    }
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
    authToken != null ? isLoggedIn.value = true : isLoggedIn.value = false;
  }
}
