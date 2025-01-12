package com.example.postItBackend.dto;

import com.example.postItBackend.model.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String username;
    private String nickname;
    private String email;

    public AuthResponseDto(Member member) {
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
    }

    @Builder
    public AuthResponseDto(String username, String nickname, String email) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }
}
