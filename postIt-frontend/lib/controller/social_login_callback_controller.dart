import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:postit_frontend/controller/main_controller.dart';

class SocialLoginCallbackController extends GetxController {
  final _storage = GetStorage();
  final MainController mainController = Get.find<MainController>();

  void saveToken(String token){
    print(":::: saveToken");
    _storage.write('token', token);

    mainController.isLoggedIn.value = true;
  }
}