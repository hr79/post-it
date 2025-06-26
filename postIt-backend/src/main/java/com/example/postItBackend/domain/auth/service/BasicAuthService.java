package com.example.postItBackend.domain.auth.service;

import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.dto.RegisterRequestDto;
import com.example.postItBackend.dto.AuthResponseDto;
import com.example.postItBackend.common.exception.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    public static final String EMAIL_REGEX = "(?=^.{4,40}$)[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private final MemberRepository memberRepository;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if (memberRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new IllegalArgumentException(ErrorMessages.USERNAME_ALREADY_IN_USE);
        }
        if (registerRequestDto.getPassword().length() < 4 || registerRequestDto.getPassword().length() > 10) {
            throw new IllegalArgumentException(ErrorMessages.PASSWORD_LENGTH_ERROR);
        }
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            throw new IllegalArgumentException(ErrorMessages.CONFIRM_PASSWORD_ERROR);
        }

        String dtoEmail = registerRequestDto.getEmail();
        String email = Optional.ofNullable(dtoEmail).orElse("anonymous@example.com");

        if (email.equals(dtoEmail)) {
            validateEmail(email);
            checkDuplicatedEmail(email);
        }

        System.out.println("==== email: " + email);

        Member member = Member.builder()
                .username(registerRequestDto.getUsername())
                .password(registerRequestDto.getPassword())
                .nickname(registerRequestDto.getNickname())
                .email(email)
                .loginType("BASIC")
                .build();

        memberRepository.save(member);

        return new AuthResponseDto(member);
    }

    private void validateEmail(String email) {
        System.out.println("==== validateEmail =======");
        Optional.of(email)
                .filter((String e) -> e.matches(EMAIL_REGEX))
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.INVALID_EMAIL));
    }

    private void checkDuplicatedEmail(String email) {
        System.out.println("==== checkDuplicatedEmail: =======");
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_ALREADY_IN_USE);
        }
    }
}

