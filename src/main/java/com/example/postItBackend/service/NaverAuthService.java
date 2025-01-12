package com.example.postItBackend.service;

import com.example.postItBackend.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class NaverAuthService implements AuthService {
    @Override
    public String getOAuth2Url() {
        return "";
    }

    @Override
    public AuthResponseDto handleOAuthCallback(String code, HttpServletResponse response) {
        return null;
    }
}
