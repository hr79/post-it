package com.example.postItBackend.controller;

import com.example.postItBackend.dto.ApiResponse;
import com.example.postItBackend.dto.ApiResponseVer3;
import com.example.postItBackend.dto.CommentRequestDto;
import com.example.postItBackend.dto.CommentResponseDto;
import com.example.postItBackend.service.CommentService;
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
//    public ApiResponse<List<CommentResponseDto>> getCommentsInPost(@PathVariable("boardId") Long id) {
    public ResponseEntity<?> getCommentsInPost(@PathVariable("boardId") Long id) {
        List<CommentResponseDto> commentsList = commentService.getCommentsInPost(id);

        return ResponseEntity.ok().body(ApiResponseVer3.success(commentsList, HttpStatus.OK.value(), "전체 댓글 가져오기"));
    }

    @PostMapping("/board/{boardId}/comment")
    public ResponseEntity<?> createComment(@PathVariable("boardId") Long id,
                                                         @RequestBody CommentRequestDto commentRequestDto,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
//        CommentResponseDto responseDto = commentService.createComment(id, commentRequestDto, userDetails);
        CommentResponseDto responseDto = commentService.createCommentVer2(id, commentRequestDto, userDetails);

//        return new ApiResponse<>(HttpStatus.CREATED.value(), "댓글을 등록했습니다.", responseDto);
        return ResponseEntity.ok().body(ApiResponseVer3.success(responseDto, HttpStatus.CREATED.value(),"comment created"));
    }

    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public ApiResponse<CommentResponseDto> deleteComment(@PathVariable("boardId") Long boardId,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        CommentResponseDto responseDto = commentService.deleteComment(commentId, userDetails, boardId);

        return new ApiResponse<>(HttpStatus.OK.value(), "댓글을 삭제했습니다.", responseDto);
    }

    @GetMapping("/comment")
    public ApiResponse<List<CommentResponseDto>> getAllComments(@AuthenticationPrincipal UserDetails userDetails) {
        List<CommentResponseDto> allComments = commentService.getAllComments(userDetails);

        return new ApiResponse<>(HttpStatus.OK.value(), "전체 댓글입니다.", allComments);
    }
}
