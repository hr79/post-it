class AppRoute {
  static final basedUrl = Uri.base.origin.contains("localhost")
      ? "http://localhost:8080/api"
      : "https://post-it-00-service.duckdns.org/api";
}
