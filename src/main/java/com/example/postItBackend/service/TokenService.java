package com.example.postItBackend.service;

import com.example.postItBackend.auth.CustomUserDetails;
import com.example.postItBackend.auth.service.RedisService;
import com.example.postItBackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public String reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("#1TokenService.reissue");
        String authorization = request.getHeader("Authorization");
        String accessToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.replaceFirst("Bearer ", "");
        }
        log.info("#2Expired Access Token: {}", accessToken);

        String username = jwtUtil.getUsernameFromExpiredToken(accessToken);
        log.info("#3 Username: {}", username);

        String refreshToken = Optional.of(username)
                .map(name -> redisService.getRefreshToken(name))
                .orElseThrow(() -> new IllegalArgumentException("Not Found Refresh Token"));
        log.info("#4 Refresh Token: {}", refreshToken);

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String newAccessToken = jwtUtil.generateAccessToken(username);
        log.info("#5 New Access Token: {}", newAccessToken);

        CustomUserDetails userDetails = new CustomUserDetails(username, null, null);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return newAccessToken;
    }

    public void logout(UserDetails userDetails, HttpServletRequest request) {
        log.info("#1TokenService.logout");
        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            throw new IllegalArgumentException("Not Found Access Token");
        }
        if (!authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Not Bearer Token");
        }

        String accessToken = authorization.replaceFirst("Bearer ", "");
        log.info("#2 Access Token: {}", accessToken);

        Long expirationTime = Optional.of(accessToken)
                .map(t -> jwtUtil.getExpirationDate(t))
                .map(d -> d.getTime() - System.currentTimeMillis())
                .orElseThrow(() -> new IllegalArgumentException("Token Expired"));
        log.info("#3 Expiration Time : {}", expirationTime);

        redisService.logoutAndBlackListToken(accessToken, expirationTime);
        redisService.delete(userDetails.getUsername());
    }
}
