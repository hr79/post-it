package com.example.postItBackend.domain.admin.post;

import com.example.postItBackend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/board")
public class AdminPostController {

    private final AdminPostService adminPostService;

    /**
     * 게시글 삭제 (관리자만 가능)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deletePostAsAdmin(@PathVariable Long boardId) {
        try {
            adminPostService.deletePost(boardId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "게시글이 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 특정 사용자의 모든 게시글 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getPostsByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(ApiResponse.success(adminPostService.getPostsByMemberId(memberId), HttpStatus.OK.value(), "사용자 게시글 조회 성공"));
    }

    /**
     * 특정 사용자의 모든 게시글 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<?> deleteAllPostsByMemberId(@PathVariable Long memberId) {
        try {
            adminPostService.deleteAllPostsByMemberId(memberId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "사용자의 모든 게시글이 삭제되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 통계: 전체 게시글 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<?> getPostsCount() {
        long count = adminPostService.getPostsCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 게시글 수 조회 성공"));
    }
}

