package com.example.postItBackend.repository;

import com.example.postItBackend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
    void deleteCommentByIdAndPostId(Long id, Long postId);
}
