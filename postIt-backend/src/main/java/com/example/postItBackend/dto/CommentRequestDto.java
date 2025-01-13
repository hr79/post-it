package com.example.postItBackend.dto;

import lombok.Getter;
import jakarta.validation.constraints.NotBlank;


@Getter
public class CommentRequestDto {
    @NotBlank
    private String content;
    @NotBlank
    private String author;
}
