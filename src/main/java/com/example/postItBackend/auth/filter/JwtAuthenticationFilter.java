package com.example.postItBackend.auth.filter;


import com.example.postItBackend.auth.service.RedisService;
import com.example.postItBackend.dto.LoginRequestDto;
import com.example.postItBackend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 로그인 시 확인 후 토큰 발급
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            System.out.println("input id = " + loginRequestDto.getUsername());
            System.out.println("input password = " + loginRequestDto.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("this is successful authentication");
        String username = authResult.getName();
        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        // HttpOnly cookie 설정
//        ResponseCookie cookie = ResponseCookie.from("jwt", accessToken)
//                .httpOnly(true)
//                .secure(false) // https에서만 적용
////                .sameSite("Strict")
//                .sameSite("None")
//                .path("/api")
//                .maxAge(3600 * 24) // 1day
//                .build();

//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setHeader("Authorization", "Bearer " + accessToken);
        log.info("accessToken = {}", accessToken);
        log.info("refreshToken = {}", refreshToken);
//        log.info("RT Size = {}", refreshToken.getBytes(StandardCharsets.UTF_8).length);
        redisService.saveRefreshToken(username, refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType("application/json");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        new ObjectMapper().writeValue(response.getOutputStream(), tokens); // token 객체를 json 타입으로 response의 출력 스트림에 작성해서 응답함
    }
}
