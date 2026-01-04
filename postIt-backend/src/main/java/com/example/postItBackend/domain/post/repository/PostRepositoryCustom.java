package com.example.postItBackend.domain.post.repository;

import com.example.postItBackend.domain.post.dto.PostDetailResponseDto;
import com.example.postItBackend.domain.post.dto.PostListPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface PostRepositoryCustom {
    void bulkUpdateViewCount(Map<Object, Object> viewCountCache);
    void bulkUpdateViewCountWithQueryDsl(Map<Long, Integer> viewCountCache);
    Page<PostListPageDto> getPostList(Pageable pageable);
    Optional<PostDetailResponseDto> findPostDetailById(Long postId);
}
