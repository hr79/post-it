package com.example.postItBackend.domain.auth.oauth.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    String getOAuth2Url();
    String handleOAuthCallback(String code, HttpServletResponse response) throws IOException;
}
