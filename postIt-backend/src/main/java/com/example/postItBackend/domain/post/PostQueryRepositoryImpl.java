package com.example.postItBackend.domain.post;

import com.example.postItBackend.dto.PostListPageDto;
import com.example.postItBackend.model.QPost;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final EntityManager entityManager;
//    private final JPAQueryFactory queryFactory;

    // 캐싱한 여러 글들의 조회수 데이터들들 쿼리 한번에 db에 업데이트하기 위해 직접 작성
    @Override
    @Transactional
    public void bulkUpdateViewCount(Map<Object, Object> viewCountCache) {
        // 쿼리 만들기
        StringBuilder queryString = new StringBuilder("UPDATE Post p SET p.viewCount = CASE ");

        for (Map.Entry<Object, Object> entry : viewCountCache.entrySet()) {
            queryString.append("WHEN p.id = :postId").append(entry.getKey())
                    .append(" THEN p.viewCount + :count").append(entry.getValue())
                    .append(" ");
            log.info(queryString.toString());
        }
        queryString.append("ELSE p.viewCount END WHERE p.id IN :postIds");
        log.info(queryString.toString());

        Query query = entityManager.createQuery(queryString.toString());

        // 쿼리문 파라미터에 값 셋팅
        for (Map.Entry<Object, Object> entry : viewCountCache.entrySet()) {
            query.setParameter("postId" + entry.getKey(), entry.getKey());
            query.setParameter("count" + entry.getValue(), entry.getValue());
            log.info(query.toString());
        }
        query.setParameter("postIds", viewCountCache.keySet()); //:postIds 매개변수에 업데이트할 게시글 ID의 목록을 설정
        query.executeUpdate();
        log.info(query.toString());

//        UPDATE Post p
//        SET p.viewCount = CASE
//            WHEN p.id = :postId1 THEN p.viewCount + :count1
//            WHEN p.id = :postId2 THEN p.viewCount + :count2
//            ELSE p.viewCount
//        END
//        WHERE p.id IN (:postIds)
    }

    @Override
    @Transactional
    public void bulkUpdateViewCountWithQueryDsl(Map<Long, Integer> viewCountCache) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPost post = QPost.post;
        // JPAUpdateClause 객체를 생성합니다.
//        UpdateClause<JPAUpdateClause> updateClause = queryFactory.update(post);
//
//        for (Map.Entry<Object, Object> entry : viewCountCache.entrySet()) {
//            Object postId = entry.getKey();
//            Integer count = (Integer) entry.getValue();
//
//            updateClause.set(post.viewCount, post.viewCount.add(count))
//                    .where(post.id.eq((Long) postId));
//            log.info(updateClause.toString());
//        }
//        updateClause.execute();
//        var caseExpression = new CaseBuilder();
        Expression<Integer> caseExpression = post.viewCount;
        for (Map.Entry<Long, Integer> entry : viewCountCache.entrySet()) {
            Long postId = (Long) entry.getKey();
            Integer count = (Integer) entry.getValue();

            // CASE WHEN 조건식
            caseExpression = new CaseBuilder()
                    .when(post.id.eq(postId))
                    .then(post.viewCount.add(count))
                    .otherwise(caseExpression);
        }
        Set<Long> postIds = viewCountCache.keySet();

        queryFactory.update(post)
                .set(post.viewCount, caseExpression) // 업데이트 대상 컬럼, 업데이트 계산식(case when)
                .where(post.id.in(postIds)) //querydsl에서 .in()은 object 타입을 지원하지 않음
                .execute();
        log.info(queryFactory.query().toString());
    }

    @Override
    public Page<PostListPageDto> getPostList(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QPost post = QPost.post;
        
        List<PostListPageDto> dtoList = queryFactory.select(Projections.constructor(
                PostListPageDto.class, post.id, post.title, post.viewCount))
                .from(post)
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}
