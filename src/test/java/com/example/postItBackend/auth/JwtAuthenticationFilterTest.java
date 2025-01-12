package com.example.postItBackend.auth;

import com.example.postItBackend.auth.service.UserDetailsServiceImpl;
import com.example.postItBackend.dto.LoginRequestDto;
import com.example.postItBackend.exception.CustomException;
import com.example.postItBackend.exception.ErrorMessages;
import com.example.postItBackend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class JwtAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    // 로그인 성공
    @Test
    public void testSuccessfulLogin() throws Exception {
        // Given
        String username = "testUser";
        String password = "testPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(new CustomUserDetails(username, password, null));
        when(jwtUtil.generateAccessToken(username)).thenReturn(accessToken);
        when(jwtUtil.generateRefreshToken(username)).thenReturn(refreshToken);

        // then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + accessToken))
                .andExpect(jsonPath("$.access_token").value(accessToken))
                .andExpect(jsonPath("$.refresh_token").value(refreshToken))
                .andDo(print());
    }

    // 잘못된 아이디 입력
    @Test
    void testLoginFail_UserNotFound() throws Exception {
        // given
        String username = "wrongUsername";
        String password = "testPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenThrow(new CustomException(ErrorMessages.USER_NOT_FOUND));

        // then
        CustomException exception = assertThrows(CustomException.class, () ->
                mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                );

        assertEquals(exception.getMessage(), ErrorMessages.USER_NOT_FOUND);
    }

    // 잘못된 비밀번호 입력
    @Test
    void testLoginFail_WrongPassword() throws Exception {
        // given
        String username = "correctUsername";
        String password = "wrongPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when
        when(userDetailsServiceImpl.loadUserByUsername(username)).thenReturn(new CustomUserDetails("correctUsername", "correctPassword", null));

        // then
        CustomException exception = assertThrows(CustomException.class, () ->
                mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
        );

        assertEquals(exception.getMessage(), ErrorMessages.PASSWORD_NOT_CORRECT);
    }
}
