package com.example.postItBackend.service;

import com.example.postItBackend.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String getOAuth2Url();
    AuthResponseDto handleOAuthCallback(String code, HttpServletResponse response);
}
