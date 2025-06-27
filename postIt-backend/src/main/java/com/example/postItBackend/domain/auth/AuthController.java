package com.example.postItBackend.domain.auth;

import com.example.postItBackend.domain.auth.service.BasicAuthService;
import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.auth.dto.RegisterRequestDto;
import com.example.postItBackend.domain.auth.dto.AuthResponseDto;
import com.example.postItBackend.domain.auth.service.TokenService;
import com.example.postItBackend.domain.auth.service.oauth2.AuthServiceFactory;
import com.example.postItBackend.domain.auth.service.oauth2.GoogleOAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceFactory authServiceFactory;
    private final TokenService tokenService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final BasicAuthService basicAuthService;

    // 가입
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDto requestDto) {
        AuthResponseDto dto = basicAuthService.register(requestDto);
        return ResponseEntity.ok()
                .body(ApiResponse.success(dto, HttpStatus.OK.value(), "Successful Register"));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String reissuedToken = tokenService.reissue(request, response);
        return ResponseEntity.ok().body(reissuedToken);
    }
}
