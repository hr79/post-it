package com.example.postItBackend.domain.comment.repository;

import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.postItBackend.domain.comment.QComment.comment;
import static com.example.postItBackend.domain.auth.model.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> findCommentsByPostIdWithProjection(Long postId) {
        return queryFactory
                .select(Projections.constructor(
                        CommentResponseDto.class,
                        comment.id,
                        comment.content,
                        member.username,
                        member.nickname,
                        comment.post.id
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.post.id.eq(postId))
                .fetch();
    }
}

