package com.example.postItBackend.domain.auth;

import com.example.postItBackend.dto.ApiResponseVer3;
import com.example.postItBackend.dto.RegisterRequestDto;
import com.example.postItBackend.dto.AuthResponseDto;
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
    private final BasicAuthService basicAuthService;

    // 가입
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequestDto requestDto) {
        AuthResponseDto dto = basicAuthService.register(requestDto);
        return ResponseEntity.ok()
                .body(ApiResponseVer3.success(dto, HttpStatus.OK.value(), "Successful Register"));
    }
}
