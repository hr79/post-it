package com.example.postItBackend.common.util;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.auth.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtil {
    private final MemberRepository memberRepository;
    private final CacheManager cacheManager;

    @Cacheable(value = "member", key = "#username")
    public Member findByUsernameWithCache(String username) {
        log.info("no member cache");
        return memberRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("member not found"));
    }

    // 게시글 조회수를 캐싱
    @CachePut(value = "viewCount", key = "#postId", cacheManager = "cacheManager")
    public Integer increaseViewCount(Long postId) {
        Integer currentViewCount = Optional
                .ofNullable(cacheManager.getCache("viewCount").get(postId, Integer.class))
                .orElse(0);
        log.info("currentViewCount: {}", currentViewCount);
        return currentViewCount + 1;
    }

    // 조회수 캐시 조회
    public Map<Long, Integer> getAllViewCountCache() {
        Cache viewCountCache = cacheManager.getCache("viewCount");
        if (viewCountCache == null) {
            log.info("view count cache is null");
            return null;
        }
        Map<Long, Integer> nativeCache = (Map<Long, Integer>) viewCountCache.getNativeCache();
        log.info("nativeCache: {}", nativeCache);
        return nativeCache;
    }

    // todo
    // 로그아웃 캐시삭제
    // 회원탈퇴 캐시삭제
}
