package com.example.postItBackend.domain.auth.oauth;

public interface OAuthUserInfo {
    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();
//    String getImageUrl();
}
