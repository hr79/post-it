package com.example.postItBackend.domain.auth.oauth.service;

import com.example.postItBackend.domain.auth.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class NaverAuthService implements AuthService {
    @Override
    public String getOAuth2Url() {
        return "";
    }

    @Override
    public String handleOAuthCallback(String code, HttpServletResponse response) {
        return null;
    }
}
