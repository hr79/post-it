package com.example.postItBackend.auth;

public interface OAuthUserInfo {
    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();
//    String getImageUrl();
}
