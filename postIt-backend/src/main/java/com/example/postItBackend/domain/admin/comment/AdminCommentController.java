package com.example.postItBackend.domain.admin.comment;

import com.example.postItBackend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/comments")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    /**
     * 모든 댓글 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(ApiResponse.success(adminCommentService.getAllComments(), HttpStatus.OK.value(), "모든 댓글 조회 성공"));
    }

//    /**
//     * 특정 게시글의 모든 댓글 조회
//     */
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<?> getCommentsByPostId(@PathVariable Long postId) {
//        return ResponseEntity.ok(ApiResponse.success(adminCommentService.getCommentsByPostId(postId), HttpStatus.OK.value(), "게시글 댓글 조회 성공"));
//    }

    /**
     * 특정 사용자의 모든 댓글 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getCommentsByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(ApiResponse.success(adminCommentService.getCommentsByMemberId(memberId), HttpStatus.OK.value(), "사용자 댓글 조회 성공"));
    }

    /**
     * 댓글 삭제 (관리자만 가능)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentAsAdmin(@PathVariable Long commentId) {
        try {
            adminCommentService.deleteComment(commentId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "댓글이 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 특정 게시글의 모든 댓글 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deleteAllCommentsByPostId(@PathVariable Long postId) {
        try {
            adminCommentService.deleteAllCommentsByPostId(postId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "게시글의 모든 댓글이 삭제되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 특정 사용자의 모든 댓글 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/member/{memberId}")
    public ResponseEntity<?> deleteAllCommentsByMemberId(@PathVariable Long memberId) {
        try {
            adminCommentService.deleteAllCommentsByMemberId(memberId);
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "사용자의 모든 댓글이 삭제되었습니다"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 통계: 전체 댓글 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<?> getCommentsCount() {
        long count = adminCommentService.getCommentsCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 댓글 수 조회 성공"));
    }
}

