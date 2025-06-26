package com.example.postItBackend.domain.comment.dto;

import com.example.postItBackend.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;
    private Long postId;

    @Builder
    public CommentResponseDto(Long id, String content, String author, Long postId) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.postId = postId;
    }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.postId = comment.getPost().getId();
    }
}
