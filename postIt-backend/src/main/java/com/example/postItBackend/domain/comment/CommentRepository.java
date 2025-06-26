package com.example.postItBackend.domain.comment;

import com.example.postItBackend.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
    void deleteCommentByIdAndPostId(Long id, Long postId);
}
