package com.example.postItBackend.domain.comment.dto;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;


@Getter
public class CommentRequestDto {
    @NotBlank
    private String content;
    @NotBlank
    private String author;
}
