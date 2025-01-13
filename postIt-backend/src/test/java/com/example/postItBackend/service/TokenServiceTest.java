package com.example.postItBackend.service;

import com.example.postItBackend.auth.service.RedisService;
import com.example.postItBackend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks
    TokenService tokenService;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    RedisService redisService;

    @Test
    void reissue(HttpServletRequest request) {
        //given
        when(request.getHeader("Authorization")).thenReturn("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJpYXQiOjE3MzM1MTU5NzAsImV4cCI6MTczMzUxNjAzMH0.fWJsbgkc9PQnqzvWH_6WJHohb-euwY88BCscqdT62C4");

    }

}