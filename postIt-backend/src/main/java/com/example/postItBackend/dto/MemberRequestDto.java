package com.example.postItBackend.dto;

import lombok.Getter;

@Getter
public class MemberRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
