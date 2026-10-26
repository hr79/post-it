package com.example.postItBackend.domain.post;

import com.example.postItBackend.common.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ViewCountUpdater {

    private final CacheUtil cacheUtil;
    private final TaskExecutor executor;

    public ViewCountUpdater(
            CacheUtil cacheUtil,
            @Qualifier("viewCountExecutor") TaskExecutor executor
    ) {
        this.cacheUtil = cacheUtil;
        this.executor = executor;
    }

    public void increaseAsync(Long postId) {
        try {
            executor.execute(() -> {
                try {
                    cacheUtil.increaseViewCount(postId);
                } catch (RuntimeException e) {
                    log.debug(
                            "조회수 갱신 실패: postId={}, exception={}",
                            postId,
                            e.getClass().getSimpleName()
                    );
                }
            });
        } catch (RuntimeException e) {
            log.debug(
                    "조회수 작업 제출 실패: postId={}, exception={}",
                    postId,
                    e.getClass().getSimpleName()
            );
        }
    }
}
