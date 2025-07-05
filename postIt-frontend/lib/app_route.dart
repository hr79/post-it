class AppRoute {
  final basedUrl = Uri.base.origin.contains("localhost")
      ? "http://localhost:8080/api"
      : "https://post-it-service.shop/api";
}
