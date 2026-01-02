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
  // RxList<Post> postList = <Post>[].obs;
  RxList<Post> postList = <Post>[].obs;
  RxInt pageNum = 0.obs;
  RxBool isLoggedIn = false.obs; // 로그인 한 상태인지
  final _storage = GetStorage();
  final isDarkMode = false.obs;

  void toggleTheme() {
    isDarkMode.value = !isDarkMode.value;
  }

  // 무한스크롤
  final ScrollController scrollController = ScrollController();
  var isLoading = false.obs;
  var hasMore = true.obs;
  int page = 0; // 현재 페이지

  getPostlist() async {
    // List<Post>? pagingPost = await _mainService.getPagingPost(pageNum.value);
    for (int i = 0; i < 20; i++) {
      List<Post> tempPostList = [];
      tempPostList.add(Post(
          id: i,
          title: "title $i",
          content:
              "content $i This is a longer content that should be truncated with ellipsis if it's too long to fit in one line.",
          viewCount: 0,
          commentCount: i % 5,
          member: null));
      if (postList.isEmpty) {
        postList.addAll(tempPostList);
      }
      print("postList = ");
      print(postList);
    }
    // postList.addAll(pagingPost!);
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

  // 로그아웃
  logOut() {
    _mainService.logOut();
    isLoggedIn.value = false;
  }

  @override
  void onInit() {
    super.onInit();
    print(":::: MainController.onInit");
    // 저장된 테마 설정 불러오기
    final savedTheme = _storage.read('isDarkMode');
    if (savedTheme != null) {
      isDarkMode.value = savedTheme;
    }
    // 테마 변경 리스너 추가
    ever(isDarkMode, (bool value) {
      _storage.write('isDarkMode', value);
      update(); // GetBuilder 업데이트
    });
    //todo 배포할때 주석해제
    getPostlist();
    // fetchInitialPosts();
    //
    // scrollController.addListener(() {
    //   if (scrollController.position.pixels >=
    //           scrollController.position.maxScrollExtent - 200 &&
    //       !isLoading.value &&
    //       hasMore.value) {
    //     fetchMorePosts();
    //   }
    // });
  }

  @override
  void onReady() {
    print(":::: MainController.onReady");
    checkLoginStatus();
  }

  // 구글 로그인
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

  // 무한스크롤 : 리스트 및 페이지 num 초기화
  void fetchInitialPosts() async {
    page = 0;
    postList.clear();
    hasMore.value = true;
    await fetchMorePosts();
  }

  Future<void> fetchMorePosts() async {
    print("::::fetchMorePosts - page: $page");
    isLoading.value = true;
    try {
      // 예시: 10개씩 가져오는 API
      final List<Post>? newPosts =
          await _mainService.fetchPostsFromBackend(page, 10);

      if (newPosts!.length < 10) {
        hasMore.value = false;
      }
      postList.addAll(newPosts);
      page++;
      print("page++ = $page");
    } catch (e) {
      print(e);
      // 에러 처리
    } finally {
      isLoading.value = false;
    }
  }
}
