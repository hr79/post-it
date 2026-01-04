package com.example.postItBackend.domain.post.dto;

import com.example.postItBackend.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String nickname;
    private int viewCount;

    @Builder
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getMember().getUsername();
        this.nickname = post.getMember().getNickname();
        this.viewCount = post.getViewCount();
    }
}
