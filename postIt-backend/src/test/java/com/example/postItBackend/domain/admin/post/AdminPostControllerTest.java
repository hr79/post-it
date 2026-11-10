package com.example.postItBackend.domain.admin.post;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.enums.UserRole;
import com.example.postItBackend.domain.post.Post;
import com.example.postItBackend.domain.post.dto.PostListPageDto;
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

@WebMvcTest(AdminPostController.class)
@Import(TestSecurityConfig.class)
@DisplayName("AdminPostController 테스트")
class AdminPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminPostService adminPostService;

    private Member testMember;
    private Post testPost;
    private PostListPageDto testPostDto;

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

        testPost = Post.builder()
                .title("테스트 게시글")
                .content("테스트 내용")
                .member(testMember)
                .build();

        testPostDto = new PostListPageDto(1L, "테스트 게시글", "테스트 내용", "테스트유저", "testuser", 0, 0);
    }

    // ==================== 모든 게시글 조회 테스트 ====================

//    @Test
    @DisplayName("ADMIN: 모든 게시글 조회 성공 (페이징)")
    @WithMockUser(roles = "ADMIN")
    void testGetAllPosts_WithAdminRole_Success() throws Exception {
        // given
        List<PostListPageDto> posts = Arrays.asList(
                testPostDto,
                new PostListPageDto(2L, "게시글2", "내용2", "user2", "user2", 0, 0)
        );
        when(adminPostService.getAllPosts(any())).thenReturn(posts);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/board")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("모든 게시글 조회 성공")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].title", is("테스트 게시글")))
                .andExpect(jsonPath("$.data[1].title", is("게시글2")));

        verify(adminPostService, times(1)).getAllPosts(any());
    }


    // ==================== 게시글 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 게시글 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeletePost_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminPostService).deletePost(1L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/board/{boardId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("게시글이 삭제되었습니다")));

        verify(adminPostService, times(1)).deletePost(1L);
    }

    @Test
    @DisplayName("USER: 게시글 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeletePost_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/board/{boardId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminPostService, never()).deletePost(anyLong());
    }

    @Test
    @DisplayName("ADMIN: 존재하지 않는 게시글 삭제 실패 (400)")
    @WithMockUser(roles = "ADMIN")
    void testDeletePost_NotFound_BadRequest() throws Exception {
        // given
        doThrow(new IllegalArgumentException("게시글을 찾을 수 없습니다"))
                .when(adminPostService).deletePost(999L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/board/{boardId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("게시글을 찾을 수 없습니다")));

        verify(adminPostService, times(1)).deletePost(999L);
    }

    // ==================== 사용자별 게시글 조회 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 게시글 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetPostsByMemberId_WithAdminRole_Success() throws Exception {
        // given
        List<Post> memberPosts = Arrays.asList(testPost);
        when(adminPostService.getPostsByMemberId(1L)).thenReturn(memberPosts);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/board/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자 게시글 조회 성공")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("테스트 게시글")));

        verify(adminPostService, times(1)).getPostsByMemberId(1L);
    }

    @Test
    @DisplayName("USER: 사용자 게시글 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetPostsByMemberId_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/board/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminPostService, never()).getPostsByMemberId(anyLong());
    }

    // ==================== 사용자별 게시글 삭제 테스트 ====================

    @Test
    @DisplayName("ADMIN: 사용자 게시글 일괄 삭제 성공")
    @WithMockUser(roles = "ADMIN")
    void testDeleteAllPostsByMemberId_WithAdminRole_Success() throws Exception {
        // given
        doNothing().when(adminPostService).deleteAllPostsByMemberId(1L);

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/board/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("사용자의 모든 게시글이 삭제되었습니다")));

        verify(adminPostService, times(1)).deleteAllPostsByMemberId(1L);
    }

    @Test
    @DisplayName("USER: 사용자 게시글 일괄 삭제 거부 (403)")
    @WithMockUser(roles = "USER")
    void testDeleteAllPostsByMemberId_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                delete("/api/admin/board/member/{memberId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminPostService, never()).deleteAllPostsByMemberId(anyLong());
    }

    // ==================== 통계 테스트 ====================

    @Test
    @DisplayName("ADMIN: 전체 게시글 수 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void testGetPostsCount_WithAdminRole_Success() throws Exception {
        // given
        when(adminPostService.getPostsCount()).thenReturn(25L);

        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/board/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", containsString("전체 게시글 수 조회 성공")))
                .andExpect(jsonPath("$.data", is(25)));

        verify(adminPostService, times(1)).getPostsCount();
    }

    @Test
    @DisplayName("USER: 전체 게시글 수 조회 거부 (403)")
    @WithMockUser(roles = "USER")
    void testGetPostsCount_WithUserRole_Forbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/api/admin/board/stats/count")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isForbidden());
        verify(adminPostService, never()).getPostsCount();
    }
}

