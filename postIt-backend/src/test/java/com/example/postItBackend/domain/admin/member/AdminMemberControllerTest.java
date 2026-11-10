package com.example.postItBackend.domain.admin.member;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.enums.UserRole;
import com.example.postItBackend.domain.admin.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminMemberController.class)
@Import(TestSecurityConfig.class)
@DisplayName("AdminMemberController 테스트")
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminMemberService adminMemberService;

    private Member testMember;
    private Member adminMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .username("testuser")
                .nickname("테스트유저")
                .email("test@example.com")
                .password("password123")
                .loginType("BASIC")
                .role(UserRole.USER)
                .build();

        adminMember = Member.builder()
                .id(2L)
                .username("admin")
                .nickname("관리자")
                .email("admin@example.com")
                .password("admin123")
                .loginType("BASIC")
                .role(UserRole.ADMIN)
                .build();
    }

    // ==================== 모든 사용자 조회 테스트 ====================

    @Test
    @DisplayName("ADMIN: 모든 사용자 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetAllMembers_WithAdminRole_Success() throws Exception {
        // given
        List<Member> members = Arrays.asList(testMember, adminMember);
        when(adminMemberService.getAllMembers()).thenReturn(members);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("모든 사용자 조회 성공")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username", is("testuser")))
                .andExpect(jsonPath("$.data[1].username", is("admin")));

        verify(adminMemberService, times(1)).getAllMembers();
    }

    @Test
    @DisplayName("USER: 모든 사용자 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetAllMembers_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).getAllMembers();
    }

    // ==================== 특정 사용자 조회 테스트 ====================

    @Test
    @DisplayName("ADMIN: 특정 사용자 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetMemberById_WithAdminRole_Success() throws Exception {
        // given
        when(adminMemberService.getMemberById(1L)).thenReturn(testMember);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자 조회 성공")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.username", is("testuser")))
                .andExpect(jsonPath("$.data.role", is("USER")));

        verify(adminMemberService, times(1)).getMemberById(1L);
    }

    @Test
    @DisplayName("USER: 특정 사용자 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetMemberById_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).getMemberById(anyLong());
    }

    // ==================== 사용자 역할 변경 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 역할 변경 성공")
    @WithMockUser(roles = "ADMIN")
    void testUpdateMemberRole_WithAdminRole_Success() throws Exception {
        // given
        Member updatedMember = Member.builder()
                .username("testuser")
                .nickname("테스트유저")
                .email("test@example.com")
                .password("password123")
                .loginType("BASIC")
                .role(UserRole.ADMIN)
                .build();

        when(adminMemberService.updateMemberRole(1L, UserRole.ADMIN)).thenReturn(updatedMember);

        String requestBody = "{\"role\":\"ADMIN\"}";

        // when
        ResultActions result = mockMvc.perform(
                put("/api/admin/members/{id}/role", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자 권한이 변경되었습니다")))
                .andExpect(jsonPath("$.data.role", is("ADMIN")));

        verify(adminMemberService, times(1)).updateMemberRole(1L, UserRole.ADMIN);
    }

    @Test
    @DisplayName("USER: 사용자 역할 변경 거부 (403)")
    @WithMockUser(roles = "USER")
    void testUpdateMemberRole_WithUserRole_Forbidden() throws Exception {
        // given
        String requestBody = "{\"role\":\"ADMIN\"}";

        // when
        ResultActions result = mockMvc.perform(
                put("/api/admin/members/{id}/role", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).updateMemberRole(anyLong(), any());
    }

    @Test
    @DisplayName("ADMIN: 유효하지 않은 역할로 변경 실패 (400)")
    @WithMockUser(roles = "ADMIN")
    void testUpdateMemberRole_InvalidRole_BadRequest() throws Exception {
        // given
        String requestBody = "{\"role\":\"INVALID\"}";

        // when
        ResultActions result = mockMvc.perform(
                put("/api/admin/members/{id}/role", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("유효하지 않은 역할")));
    }

    // ==================== 사용자 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 삭제 성공")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteMember_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminMemberService).deleteMember(1L, "admin");

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자가 삭제되었습니다")));

        verify(adminMemberService, times(1)).deleteMember(1L, "admin");
    }

    @Test
    @DisplayName("USER: 사용자 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeleteMember_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/members/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).deleteMember(anyLong(), anyString());
    }

    @Test
    @DisplayName("ADMIN: 본인 삭제 실패 (400)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteMember_DeleteSelf_BadRequest() throws Exception {
        // given
        doThrow(new IllegalArgumentException("본인을 삭제할 수 없습니다"))
                .when(adminMemberService).deleteMember(2L, "admin");

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/members/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("본인을 삭제할 수 없습니다")));

        verify(adminMemberService, times(1)).deleteMember(2L, "admin");
    }

    // ==================== 통계 테스트 ====================

    @Test
    @DisplayName("ADMIN: 전체 사용자 수 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetMembersCount_WithAdminRole_Success() throws Exception {
        // given
        when(adminMemberService.getMembersCount()).thenReturn(10L);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("전체 사용자 수 조회 성공")))
                .andExpect(jsonPath("$.data", is(10)));

        verify(adminMemberService, times(1)).getMembersCount();
    }

    @Test
    @DisplayName("USER: 전체 사용자 수 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetMembersCount_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).getMembersCount();
    }

    @Test
    @DisplayName("ADMIN: ADMIN 사용자 수 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetAdminCount_WithAdminRole_Success() throws Exception {
        // given
        when(adminMemberService.getAdminCount()).thenReturn(3L);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/admin-count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("ADMIN 사용자 수 조회 성공")))
                .andExpect(jsonPath("$.data", is(3)));

        verify(adminMemberService, times(1)).getAdminCount();
    }

    @Test
    @DisplayName("USER: ADMIN 사용자 수 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetAdminCount_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/admin-count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).getAdminCount();
    }

    @Test
    @DisplayName("ADMIN: USER 사용자 수 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetUserCount_WithAdminRole_Success() throws Exception {
        // given
        when(adminMemberService.getUserCount()).thenReturn(7L);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/user-count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("USER 사용자 수 조회 성공")))
                .andExpect(jsonPath("$.data", is(7)));

        verify(adminMemberService, times(1)).getUserCount();
    }

    @Test
    @DisplayName("USER: USER 사용자 수 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetUserCount_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/members/stats/user-count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminMemberService, never()).getUserCount();
    }
}

