package com.example.postItBackend.domain.admin;

import com.example.postItBackend.common.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

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

