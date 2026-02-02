package com.example.postItBackend.domain.post.repository;

import com.example.postItBackend.domain.auth.model.QMember;
import com.example.postItBackend.domain.post.dto.PostDetailResponseDto;
import com.example.postItBackend.domain.post.dto.PostListPageDto;
import com.example.postItBackend.domain.post.QPost;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void bulkUpdateViewCountWithQueryDsl(Map<Long, Integer> viewCountCache) {
        if (viewCountCache.isEmpty()) {
            log.info(":::: 조회수 캐시가 없습니다.");
            return;
        }

        QPost post = QPost.post;
        Set<Long> postIds = viewCountCache.keySet();

        // CaseBuilder를 사용한 CASE WHEN 구성
        CaseBuilder caseBuilder = new CaseBuilder();
        var caseExpression = caseBuilder
                .when(post.id.eq(postIds.iterator().next()))
                .then(post.viewCount.add(viewCountCache.get(postIds.iterator().next())));

        for (Map.Entry<Long, Integer> entry : viewCountCache.entrySet()) {
            caseExpression = caseExpression
                    .when(post.id.eq(entry.getKey()))
                    .then(post.viewCount.add(entry.getValue()));
        }

        Expression<Integer> finalExpression = caseExpression.otherwise(post.viewCount);

        // QueryDSL을 사용한 벌크 업데이트
        long updatedCount = queryFactory.update(post)
                .set(post.viewCount, finalExpression)
                .where(post.id.in(postIds))
                .execute();

        log.info(":::: Updated {} posts' viewCount", updatedCount);
    }


    @Override
    public Page<PostListPageDto> getPostList(Pageable pageable) {
        QPost post = QPost.post;

        List<PostListPageDto> dtoList = queryFactory.select(Projections.constructor(
                        PostListPageDto.class,
                        post.id, post.title, post.content, post.member.nickname, post.member.username, post.viewCount, post.commentCount))
                .from(post)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Optional<PostDetailResponseDto> findPostDetailById(Long postId) {
        QPost post = QPost.post;
        QMember member = QMember.member;

        return Optional.ofNullable(
                queryFactory
                        .select(Projections.constructor(
                                PostDetailResponseDto.class,
                                post.id,
                                post.title,
                                post.content,
                                post.viewCount,
                                member.username,
                                member.nickname
                        ))
                        .from(post)
                        .join(post.member, member)
                        .where(post.id.eq(postId))
                        .fetchOne()
        );
    }
}
