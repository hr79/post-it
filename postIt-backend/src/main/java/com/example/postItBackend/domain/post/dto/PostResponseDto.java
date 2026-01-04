package com.example.postItBackend.domain.post.dto;

import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.example.postItBackend.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String nickname;
    private int viewCount;
    private List<CommentResponseDto> comments;

    @Builder
    public PostResponseDto(Post post, List<CommentResponseDto> commentsDto) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getMember().getUsername();
        this.nickname = post.getMember().getNickname();
        this.viewCount = post.getViewCount();
        this.comments = commentsDto;
    }
    public PostResponseDto(PostListPageDto postListPageDto) {
        this.id = postListPageDto.getId();
        this.title = postListPageDto.getTitle();
        this.nickname = postListPageDto.getNickname();
        this.viewCount = postListPageDto.getViewCount();
    }
}
