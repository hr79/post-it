package com.example.postItBackend.auth;

public class GoogleUserInfo implements OAuthUserInfo {
    private String provider;
    private String providerId;
    private String name;
    private String email;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }
}
