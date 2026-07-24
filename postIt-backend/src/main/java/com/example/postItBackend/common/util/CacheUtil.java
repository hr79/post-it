package com.example.postItBackend.common.util;

import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.auth.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public void increaseViewCount(Long postId) {
        String key = "viewCount::" + postId;
        log.info("key: {}", key);

        Integer currentViewCount = 0;
        if (redisTemplate.opsForValue().get(key) != null) {
            currentViewCount = (Integer) redisTemplate.opsForValue().get(key);
        }
        Integer newViewCount = currentViewCount + 1;
        redisTemplate.opsForValue().set(key, newViewCount);
        log.info("updated ViewCount: {}", newViewCount);
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
            Integer currentViewCount = (Integer) redisTemplate.opsForValue().get(key);
            resultCache.put(postId, currentViewCount);
        });
        log.info("resultCache: {}", resultCache);

        return resultCache;
    }

    public void clearAllViewCountCache() {
        Set<String> keys = redisTemplate.keys("viewCount::*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    // todo
    // 로그아웃 캐시삭제
    // 회원탈퇴 캐시삭제
}
