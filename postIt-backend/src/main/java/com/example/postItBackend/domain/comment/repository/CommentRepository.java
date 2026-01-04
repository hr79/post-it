package com.example.postItBackend.domain.comment.repository;

import com.example.postItBackend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    List<Comment> findAllByPostId(Long postId);
    void deleteCommentByIdAndPostId(Long id, Long postId);
}
