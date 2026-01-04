import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/controller/auth_controller.dart';
import 'package:postit_frontend/model/post.dart';
import 'package:postit_frontend/service/main_service.dart';

class MainController extends GetxController {
  final MainService _mainService = MainService();
  // final authController = Get.find<AuthController>();

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
    List<Post>? pagingPost = await _mainService.getPagingPost(pageNum.value);
    postList.addAll(pagingPost!);

    if (postList.isEmpty) {
      List<Post> tempPostList = [];
      for (int i = 0; i < 20; i++) {
        tempPostList.add(Post(
          id: i,
          title: "title $i",
          content:
              "content $i This is a longer content that should be truncated with ellipsis if it's too long to fit in one line.",
          viewCount: 0,
          commentCount: i % 5,
          username: "user$i",
          nickname: "user$i",
        ));
      }

      postList.addAll(tempPostList);
    }
    print("postList = ");
    print(postList);
    pageNum.value++;
  }

  logOut() {
    // authController.logOut();
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
