package com.example.postItBackend.controller;

import com.example.postItBackend.dto.ApiResponseVer3;
import com.example.postItBackend.dto.RegisterRequestDto;
import com.example.postItBackend.dto.AuthResponseDto;
import com.example.postItBackend.model.LoginType;
import com.example.postItBackend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController extends BaseController<AuthResponseDto> {
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
                .body(ApiResponseVer3.success(dto, HttpStatus.OK.value(), "Successful Register"));
    }

    // 소셜로그인
    @GetMapping("/oauth2-login")
    public ResponseEntity<?> oauth2Login(@RequestParam(value = "login_type", required = false) String loginType) {
        log.info("loginType : {}", loginType);
        LoginType loginTypeEnum = LoginType.valueOf(loginType.toUpperCase());
        String oAuth2Url = authServiceFactory.getAuthService(loginTypeEnum).getOAuth2Url();

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create(oAuth2Url)).build();
    }

    // google auth auto-redirect callback
    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code,
                           @RequestParam(value = "state", required = false) String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        log.info("requestURI : {}", request.getRequestURL());
        AuthResponseDto dto = googleOAuth2Service.handleOAuthCallback(code, response);

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(URI.create("http://13.209.85.84/")).build();
//                .body(ApiResponseVer3.success(dto, HttpStatus.OK.value(), "Successful SNS Login"));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String reissuedToken = tokenService.reissue(request, response);
        return ResponseEntity.ok().body(reissuedToken);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        tokenService.logout(userDetails, request);

        return ResponseEntity.ok().body(userDetails.getUsername() + " 로그아웃 완료");
    }
}
