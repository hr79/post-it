package com.example.postItBackend.domain.auth.oauth.service;

import com.example.postItBackend.domain.auth.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String getOAuth2Url();
    AuthResponseDto handleOAuthCallback(String code, HttpServletResponse response);
}
