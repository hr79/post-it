package com.example.postItBackend.domain.comment.repository;

import com.example.postItBackend.domain.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentResponseDto> findCommentsByPostIdWithProjection(Long postId);
}

