package com.example.postItBackend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterRequestDto {
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String confirmPassword;
    private String nickname;
    private String email;

    @Builder
    public RegisterRequestDto(String username, String password, String confirmPassword, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.nickname = nickname;
        this.email = email;
    }
}