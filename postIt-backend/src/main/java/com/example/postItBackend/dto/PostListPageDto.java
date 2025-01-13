package com.example.postItBackend.dto;

import lombok.Getter;

@Getter
public class PostListPageDto {
    private Long id;
    private String title;
    private int viewCount;
//    private int commentCount;

    public PostListPageDto(Long id, String title, int viewCount) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
//        this.commentCount = commentCount;
    }
}
