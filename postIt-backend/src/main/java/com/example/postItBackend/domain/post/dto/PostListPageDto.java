package com.example.postItBackend.domain.post.dto;

import lombok.Getter;

@Getter
public class PostListPageDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String username;
    private int viewCount;
    private int commentCount;

    public PostListPageDto(Long id, String title, String content,String nickname, String username, int viewCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.username = username;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }
}
