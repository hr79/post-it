package com.example.postItBackend.domain.auth;

import com.example.postItBackend.domain.auth.service.BasicAuthService;
import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.auth.dto.RegisterRequestDto;
import com.example.postItBackend.domain.auth.dto.AuthResponseDto;
import com.example.postItBackend.domain.auth.service.TokenService;
import com.example.postItBackend.domain.auth.oauth.service.AuthServiceFactory;
import com.example.postItBackend.domain.auth.oauth.service.GoogleOAuth2Service;
import com.example.postItBackend.domain.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthServiceFactory authServiceFactory;
    private final TokenService tokenService;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final BasicAuthService basicAuthService;

    @Value("${main-page}")
    private String mainPage;

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

    // 소셜로그인
    @GetMapping("/oauth2-login")
    public ResponseEntity<String> oauth2Login(@RequestParam(value = "login_type", required = false) String loginType, HttpServletResponse response) throws IOException {
        log.info("loginType : {}", loginType);
        LoginType loginTypeEnum = LoginType.valueOf(loginType.toUpperCase());
        String oAuth2Url = authServiceFactory.getAuthService(loginTypeEnum).getOAuth2Url();

        return ResponseEntity.ok().body(oAuth2Url);
    }

    // google auth auto-redirect callback
    @GetMapping("/oauth2/callback")
    public void callback(@RequestParam("code") String code,
                                      @RequestParam(value = "state", required = false) String state,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        log.info("requestURI : {}", request.getRequestURL());
        String accessToken = googleOAuth2Service.handleOAuthCallback(code, response);
        log.info("mainPage : {}", mainPage);

//        Flutter (현재탭) → loginUrl 접속
//↓
//        Google 로그인 → Spring 콜백
//↓
//        Spring: accessToken 발급 → Flutter callback URL로 리다이렉트
//↓
//        Flutter callback 페이지에서 토큰 저장 → 홈으로 리다이렉트

        String redirectUrl = mainPage + "/#/auth/social/callback?access_token=" + accessToken;
        log.info("redirectUrl : {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        tokenService.logout(userDetails, request);

        return ResponseEntity.ok().body(userDetails.getUsername() + " 로그아웃 완료");
    }
}
