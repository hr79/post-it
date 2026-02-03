package com.example.postItBackend.common.util;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.auth.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtil {
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "member", key = "#username")
    public Member findByUsernameWithCache(String username) {
        log.info("no member cache");
        return memberRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("member not found"));
    }

    // 게시글 조회수를 캐싱
    @CachePut(value = "viewCount", key = "#postId")
    public Integer increaseViewCount(Long postId) {
        String key = "viewCount::" + postId;
        Integer currentViewCount = Optional
                .ofNullable((Integer) redisTemplate.opsForValue().get(key))
                .orElse(0);
        Integer newViewCount = currentViewCount + 1;
        redisTemplate.opsForValue().set(key, newViewCount);
        log.info("currentViewCount: {}", newViewCount);
        return newViewCount;
    }

    // 조회수 캐시 조회
    public Map<Long, Integer> getAllViewCountCache() {
        Set<String> keys = redisTemplate.keys("viewCount::*");

        if (keys.isEmpty()) {
            log.info("view count cache is empty");
            return new HashMap<>();
        }

        Map<Long, Integer> resultCache = new HashMap<>();

        keys.forEach(key -> {
            Long postId = Long.parseLong(key.split("::")[1]);
            Integer count = (Integer) redisTemplate.opsForValue().get(key);
            if (count != null) {
                resultCache.put(postId, count);
            }
        });

        log.info("resultCache: {}", resultCache);

        return resultCache;
    }

    // todo
    // 로그아웃 캐시삭제
    // 회원탈퇴 캐시삭제
}
