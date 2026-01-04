package com.example.postItBackend.domain.post.repository;

import com.example.postItBackend.domain.post.Post;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @EntityGraph(attributePaths = {"comments", "member"}) List<Post> findAll();
    Page<Post> findAll(Pageable pageable);
    void deleteById(@Nullable Long id);

    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findById(@Nullable Long id);

//    @Transactional @Modifying @Query("update Post p set p.viewCount = p.viewCount + :count where p.id = :postId")
//    void updateViewCount(Long postId, int count); // 조회수 업데이트
}
