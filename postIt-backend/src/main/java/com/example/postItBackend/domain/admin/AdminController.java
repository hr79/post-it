package com.example.postItBackend.domain.admin;

import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 모든 사용자 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllMembers(), HttpStatus.OK.value(), "모든 사용자 조회 성공"));
    }

    /**
     * 특정 사용자 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        Member member = adminService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(member, HttpStatus.OK.value(), "사용자 조회 성공"));
    }

    /**
     * 사용자 권한 변경 (ADMIN/USER)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/members/{id}/role")
    public ResponseEntity<?> updateMemberRole(@PathVariable Long id, @RequestBody RoleUpdateRequest roleRequest) {
        try {
            UserRole newRole = UserRole.valueOf(roleRequest.getRole().toUpperCase());
            Member member = adminService.updateMemberRole(id, newRole);
            return ResponseEntity.ok(ApiResponse.success(member, HttpStatus.OK.value(), "사용자 권한이 변경되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, "유효하지 않은 역할입니다"));
        }
    }

    /**
     * 사용자 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            adminService.deleteMember(id, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "사용자가 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 모든 게시글 조회 (페이징)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/board")
    public ResponseEntity<?> getAllPosts(@RequestParam(required = false, value = "page", defaultValue = "0") int pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 20, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllPosts(pageRequest), HttpStatus.OK.value(), "모든 게시글 조회 성공"));
    }

    /**
     * 특정 게시글 삭제 (관리자만 가능)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deletePostAsAdmin(@PathVariable("boardId") Long id) {
        try {
            adminService.deletePost(id);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "게시글이 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 모든 댓글 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/comments")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAllComments(), HttpStatus.OK.value(), "모든 댓글 조회 성공"));
    }

    /**
     * 특정 댓글 삭제 (관리자만 가능)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteCommentAsAdmin(@PathVariable Long commentId) {
        try {
            adminService.deleteComment(commentId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "댓글이 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 통계: 전체 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/members-count")
    public ResponseEntity<?> getMembersCount() {
        long count = adminService.getMembersCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 사용자 수 조회 성공"));
    }

    /**
     * 통계: 전체 게시글 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/posts-count")
    public ResponseEntity<?> getPostsCount() {
        long count = adminService.getPostsCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 게시글 수 조회 성공"));
    }

    /**
     * 통계: 전체 댓글 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/comments-count")
    public ResponseEntity<?> getCommentsCount() {
        long count = adminService.getCommentsCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 댓글 수 조회 성공"));
    }

    /**
     * 통계: ADMIN 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/admin-count")
    public ResponseEntity<?> getAdminCount() {
        long count = adminService.getAdminCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "ADMIN 사용자 수 조회 성공"));
    }

    /**
     * 통계: USER 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/user-count")
    public ResponseEntity<?> getUserCount() {
        long count = adminService.getUserCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "USER 사용자 수 조회 성공"));
    }

    /**
     * 권한 변경 요청 DTO
     */
    @Getter
    @Setter
    public static class RoleUpdateRequest {
        private String role;
    }
}

