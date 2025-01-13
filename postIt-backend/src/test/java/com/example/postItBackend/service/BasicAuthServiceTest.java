package com.example.postItBackend.service;

import com.example.postItBackend.dto.RegisterRequestDto;
import com.example.postItBackend.dto.AuthResponseDto;
import com.example.postItBackend.exception.CustomException;
import com.example.postItBackend.exception.ErrorMessages;
import com.example.postItBackend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @InjectMocks
    private BasicAuthService basicAuthService;

    @Mock
    private MemberRepository memberRepository;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    // 회원가입 성공
    @Test
    void successRegistration() {
        //given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("password")
                .nickname("testnickname")
                .build();

        when(memberRepository.existsByUsername(requestDto.getUsername())).thenReturn(false);

        //when
        AuthResponseDto responseDto = basicAuthService.register(requestDto);

        //then
        assertNotNull(responseDto);
        assertEquals(requestDto.getUsername(), responseDto.getUsername());
    }

    // 아이디 중복으로 실패
    @Test
    void failRegistration_DuplicatedUsername() {
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("password")
                .nickname("testnickname")
                .build();

        when(memberRepository.existsByUsername(registerRequestDto.getUsername())).thenReturn(true);

        // when & then
        CustomException customException = assertThrows(
                CustomException.class, () -> basicAuthService.register(registerRequestDto));
        assertEquals(customException.getMessage(), ErrorMessages.USERNAME_ALREADY_IN_USE);
    }

    // 비밀번호 조건 부합하지 않음
    @Test
    void failRegistration_Password_Length_Error() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("toolongpassword")
                .confirmPassword("toolongpassword")
                .nickname("testnickname")
                .build();

        // when & then
        CustomException exception = assertThrows(
                CustomException.class, () -> basicAuthService.register(requestDto));
        assertEquals(exception.getMessage(), ErrorMessages.PASSWORD_LENGTH_ERROR);
    }

    // 비밀번호 재입력 불일치
    @Test
    void failRegistration_ConfirmPassword_Error() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("confirmpassword")
                .nickname("testnickname")
                .build();

        // when & then
        CustomException exception = assertThrows(
                CustomException.class, () -> basicAuthService.register(requestDto));
        assertEquals(exception.getMessage(), ErrorMessages.CONFIRM_PASSWORD_ERROR);
    }

    // email이 있을 경우, 없을 경우
    @ParameterizedTest
    @ValueSource(strings = {"test@example.com"})
    @NullSource
    void register_WithAndWithoutEmail(String email) {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("password")
                .nickname("testnickname")
                .email(email)
                .build();

        // when
        AuthResponseDto responseDto = basicAuthService.register(requestDto);

        // then
        if (email != null) {
            assertEquals(responseDto.getEmail(), email);
        } else {
            assertEquals(responseDto.getEmail(), "anonymous@example.com");
        }
    }

    // email이 유효하지 않을 경우
    @Test
    void failRegistration_InvalidEmail() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("password")
                .nickname("testnickname")
                .email("invalidemail")
                .build();

        // when & then
        CustomException exception = assertThrows(
                CustomException.class, () -> basicAuthService.register(requestDto));
        assertEquals(exception.getMessage(), ErrorMessages.INVALID_EMAIL);
    }

    // 이미 사용중인 email
    @Test
    void failRegistration_EmailAlreadyInUse() {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto
                .builder()
                .username("testusername")
                .password("password")
                .confirmPassword("password")
                .nickname("testnickname")
                .email("test@example.com")
                .build();

        when(memberRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> basicAuthService.register(requestDto));
        assertEquals(exception.getMessage(), ErrorMessages.EMAIL_ALREADY_IN_USE);
    }
}