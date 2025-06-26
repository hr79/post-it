package com.example.postItBackend.domain.comment;

import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.comment.dto.CommentRequestDto;
import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/board/{boardId}/comment")
    public ResponseEntity<?> getCommentsInPost(@PathVariable("boardId") Long id) {
        List<CommentResponseDto> commentsList = commentService.getCommentsInPost(id);

        return ResponseEntity.ok().body(ApiResponse.success(commentsList, HttpStatus.OK.value(), "전체 댓글 가져오기"));
    }

    @PostMapping("/board/{boardId}/comment")
    public ResponseEntity<?> createComment(@PathVariable("boardId") Long id,
                                                         @RequestBody CommentRequestDto commentRequestDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.createComment(id, commentRequestDto, userDetails);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto, HttpStatus.CREATED.value(),"comment created"));
    }

    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable("boardId") Long boardId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.deleteComment(commentId, userDetails, boardId);

        return ResponseEntity.ok(ApiResponse.success(responseDto, HttpStatus.OK.value(), "댓글을 삭제했습니다."));
    }

    @GetMapping("/comment")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getAllComments(@AuthenticationPrincipal UserDetails userDetails) {
        List<CommentResponseDto> allComments = commentService.getAllComments(userDetails);

        return ResponseEntity.ok().body(ApiResponse.success(allComments, HttpStatus.CREATED.value(),"comment created"));
    }
}
