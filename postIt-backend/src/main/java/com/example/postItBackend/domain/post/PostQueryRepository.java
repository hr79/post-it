package com.example.postItBackend.domain.post;

import com.example.postItBackend.dto.PostListPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PostQueryRepository {
    void bulkUpdateViewCount(Map<Object, Object> viewCountCache);
    void bulkUpdateViewCountWithQueryDsl(Map<Long, Integer> viewCountCache);
    Page<PostListPageDto> getPostList(Pageable pageable);
}
