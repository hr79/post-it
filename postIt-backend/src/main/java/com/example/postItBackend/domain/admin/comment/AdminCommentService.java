package com.example.postItBackend.domain.admin.comment;

import com.example.postItBackend.domain.comment.Comment;
import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.example.postItBackend.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    /**
     * 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments() {
        List<Comment> all = commentRepository.findAll();
        return all.stream().map(CommentResponseDto::new).toList();
    }

    /**
     * 특정 게시글의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.findCommentsByPostIdWithProjection(postId);
    }

    /**
     * 특정 사용자의 모든 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByMemberId(Long memberId) {
        List<Comment> all = commentRepository.findAll();
        return all.stream()
                .filter(comment -> comment.getMember().getId().equals(memberId))
                .map(CommentResponseDto::new)
                .toList();
    }

    /**
     * 댓글 삭제 (관리자 권한으로 아무 댓글 삭제 가능)
     */
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("댓글을 찾을 수 없습니다");
        }
        commentRepository.deleteById(commentId);
        log.info("Comment {} deleted by admin", commentId);
    }

    /**
     * 특정 게시글의 모든 댓글 삭제
     */
    @Transactional
    public void deleteAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getPost().getId().equals(postId))
                .toList();
        commentRepository.deleteAll(comments);
        log.info("All comments for post {} deleted by admin", postId);
    }

    /**
     * 특정 사용자의 모든 댓글 삭제
     */
    @Transactional
    public void deleteAllCommentsByMemberId(Long memberId) {
        List<Comment> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getMember().getId().equals(memberId))
                .toList();
        commentRepository.deleteAll(comments);
        log.info("All comments for member {} deleted by admin", memberId);
    }

    /**
     * 전체 댓글 수
     */
    @Transactional(readOnly = true)
    public long getCommentsCount() {
        return commentRepository.count();
    }
}

