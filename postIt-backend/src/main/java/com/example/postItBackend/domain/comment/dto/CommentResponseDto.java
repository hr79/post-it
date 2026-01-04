package com.example.postItBackend.domain.comment.dto;

import com.example.postItBackend.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private String nickname;
    private Long postId;

    @Builder
    public CommentResponseDto(Long id, String content, String username, String nickname, Long postId) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.nickname = nickname;
        this.postId = postId;
    }

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getMember().getUsername();
        this.nickname = comment.getMember().getNickname();
        this.postId = comment.getPost().getId();
    }
}
