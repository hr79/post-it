package com.example.postItBackend.service;

import com.example.postItBackend.model.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceFactory {
    private final NaverAuthService naverAuthService;
    private final GoogleOAuth2Service googleOAuth2Service;

    public AuthService getAuthService(LoginType loginType) {
        return switch (loginType) {
            case NAVER -> naverAuthService;
            case GOOGLE -> googleOAuth2Service;
        };
    }
}
