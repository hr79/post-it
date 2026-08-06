package com.example.postItBackend.domain.admin.comment;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
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

@WebMvcTest(AdminCommentController.class)
@Import(TestSecurityConfig.class)
@DisplayName("AdminCommentController 테스트")
class AdminCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminCommentService adminCommentService;

    private Member testMember;
    private CommentResponseDto testCommentDto;

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

        testCommentDto = CommentResponseDto.builder()
                .id(1L)
                .content("테스트 댓글")
                .username("testuser")
                .nickname("테스트유저")
                .postId(1L)
                .build();
    }

    // ==================== 모든 댓글 조회 테스트 ====================

    @Test
    @DisplayName("ADMIN: 모든 댓글 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetAllComments_WithAdminRole_Success() throws Exception {
        // given
        List<CommentResponseDto> comments = Arrays.asList(
                testCommentDto,
                CommentResponseDto.builder()
                        .id(2L)
                        .content("테스트 댓글2")
                        .username("user2")
                        .nickname("user2")
                        .postId(1L)
                        .build()
        );
        when(adminCommentService.getAllComments()).thenReturn(comments);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("모든 댓글 조회 성공")))
                .andExpect(jsonPath("$.data", hasSize(2)));

        verify(adminCommentService, times(1)).getAllComments();
    }

    @Test
    @DisplayName("USER: 모든 댓글 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetAllComments_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).getAllComments();
    }

    // ==================== 사용자별 댓글 조회 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 댓글 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetCommentsByMemberId_WithAdminRole_Success() throws Exception {
        // given
        List<CommentResponseDto> memberComments = Arrays.asList(testCommentDto);
        when(adminCommentService.getCommentsByMemberId(1L)).thenReturn(memberComments);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자 댓글 조회 성공")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(adminCommentService, times(1)).getCommentsByMemberId(1L);
    }

    @Test
    @DisplayName("USER: 사용자 댓글 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetCommentsByMemberId_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).getCommentsByMemberId(anyLong());
    }

    // ==================== 댓글 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 댓글 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeleteComment_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminCommentService).deleteComment(1L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("댓글이 삭제되었습니다")));

        verify(adminCommentService, times(1)).deleteComment(1L);
    }

    @Test
    @DisplayName("USER: 댓글 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeleteComment_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).deleteComment(anyLong());
    }

    @Test
    @DisplayName("ADMIN: 존재하지 않는 댓글 삭제 실패 (400)")
    @WithMockUser(roles = "ADMIN")
    void testDeleteComment_NotFound_BadRequest() throws Exception {
        // given
        doThrow(new IllegalArgumentException("댓글을 찾을 수 없습니다"))
                .when(adminCommentService).deleteComment(999L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/{commentId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("댓글을 찾을 수 없습니다")));

        verify(adminCommentService, times(1)).deleteComment(999L);
    }

    // ==================== 게시글별 댓글 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 게시글 댓글 일괄 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeleteAllCommentsByPostId_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminCommentService).deleteAllCommentsByPostId(1L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("게시글의 모든 댓글이 삭제되었습니다")));

        verify(adminCommentService, times(1)).deleteAllCommentsByPostId(1L);
    }

    @Test
    @DisplayName("USER: 게시글 댓글 일괄 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeleteAllCommentsByPostId_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/post/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).deleteAllCommentsByPostId(anyLong());
    }

    // ==================== 사용자별 댓글 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 댓글 일괄 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeleteAllCommentsByMemberId_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminCommentService).deleteAllCommentsByMemberId(1L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자의 모든 댓글이 삭제되었습니다")));

        verify(adminCommentService, times(1)).deleteAllCommentsByMemberId(1L);
    }

    @Test
    @DisplayName("USER: 사용자 댓글 일괄 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeleteAllCommentsByMemberId_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/comments/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).deleteAllCommentsByMemberId(anyLong());
    }

    // ==================== 통계 테스트 ====================

    @Test
    @DisplayName("ADMIN: 전체 댓글 수 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetCommentsCount_WithAdminRole_Success() throws Exception {
        // given
        when(adminCommentService.getCommentsCount()).thenReturn(50L);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("전체 댓글 수 조회 성공")))
                .andExpect(jsonPath("$.data", is(50)));

        verify(adminCommentService, times(1)).getCommentsCount();
    }

    @Test
    @DisplayName("USER: 전체 댓글 수 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetCommentsCount_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/comments/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminCommentService, never()).getCommentsCount();
    }
}

