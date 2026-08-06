package com.example.postItBackend.domain.admin.post;

import com.example.postItBackend.domain.post.Post;
import com.example.postItBackend.domain.post.dto.PostListPageDto;
import com.example.postItBackend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final PostRepository postRepository;

    /**
     * 모든 게시글 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public List<PostListPageDto> getAllPosts(Pageable pageable) {
        Page<PostListPageDto> postList = postRepository.getPostList(pageable);
        return postList.toList();
    }

    /**
     * 특정 게시글 조회
     */
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));
    }

    /**
     * 게시글 삭제 (관리자 권한으로 아무 게시글 삭제 가능)
     */
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다");
        }
        postRepository.deleteById(id);
        log.info("Post {} deleted by admin", id);
    }

    /**
     * 특정 사용자의 모든 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByMemberId(Long memberId) {
        return postRepository.findAll().stream()
                .filter(post -> post.getMember().getId().equals(memberId))
                .toList();
    }

    /**
     * 특정 사용자의 모든 게시글 삭제
     */
    @Transactional
    public void deleteAllPostsByMemberId(Long memberId) {
        List<Post> posts = postRepository.findAll().stream()
                .filter(post -> post.getMember().getId().equals(memberId))
                .toList();
        postRepository.deleteAll(posts);
        log.info("All posts for member {} deleted by admin", memberId);
    }

    /**
     * 전체 게시글 수
     */
    @Transactional(readOnly = true)
    public long getPostsCount() {
        return postRepository.count();
    }
}

