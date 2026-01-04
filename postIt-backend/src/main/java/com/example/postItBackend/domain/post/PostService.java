package com.example.postItBackend.domain.post;

import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.example.postItBackend.domain.post.dto.PostListPageDto;
import com.example.postItBackend.domain.post.dto.PostRequestDto;
import com.example.postItBackend.domain.post.dto.PostResponseDto;
import com.example.postItBackend.domain.auth.model.Member;

import com.example.postItBackend.common.util.CacheUtil;
import com.example.postItBackend.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PostService {

    private final PostRepository postRepository;
    private final CacheUtil cacheUtil;

    public PostService(PostRepository postRepository, CacheUtil cacheUtil) {
        this.postRepository = postRepository;
        this.cacheUtil = cacheUtil;
    }

    @Transactional(readOnly = true)
    public List<PostListPageDto> getAllPosts(Pageable pageable) {
        Page<PostListPageDto> postList = postRepository.getPostList(pageable);

        return postList.toList();
//        return postList.stream().map(post -> new PostResponseDto(post)).toList();
    }

    @Transactional
    public PostResponseDto savePost(PostRequestDto postRequestDto, UserDetails userDetails) {
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Member member = cacheUtil.findByUsernameWithCache(userDetails.getUsername());

        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();

        postRepository.save(post);

        return PostResponseDto.builder().post(post).build();
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Post"));
        cacheUtil.increaseViewCount(id); // 조회된 글의 조회수 증가(caching)
        List<CommentResponseDto> commentResponseDtoList = post.getComments().stream()
                .map(c -> new CommentResponseDto(c))
                .toList();

        return new PostResponseDto(post, commentResponseDtoList);
    }

    @Transactional
    public PostResponseDto deletePost(Long id, UserDetails userDetails) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Post"));

        Optional.of(post)
                .map(p -> p.getMember().getUsername())
                .map(u -> u.equals(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("You do not have permission to delete this post"));

        postRepository.deleteById(id);

        return PostResponseDto.builder()
                .post(post)
                .build();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, UserDetails userDetails) {
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Post"));

        if (!post.getMember().getUsername().equals(userDetails.getUsername())) {
            throw new IllegalArgumentException("You do not have permission to update this post");
        }

        log.info("==== before : " + post.getTitle());
        log.info("==== before : " + post.getContent());

        Post updatedPost = post.update(title, content);

        log.info("==== after : " + post.getTitle());
        log.info("==== after : " + post.getContent());

        return PostResponseDto.builder()
                .post(updatedPost)
                .build();
    }

    // 조회수 업데이트
    @Scheduled(cron = "0 */3 * * * *")
    @CacheEvict(value = "viewCount", allEntries = true, beforeInvocation = false) // 캐싱된 데이터들을 db 업데이트 후 캐시 삭제
    @Transactional
    public void updateViewCount() {
        log.info(":::: updateViewCount");
        Map<Long, Integer> allViewCountCache = cacheUtil.getAllViewCountCache();
        if (!allViewCountCache.isEmpty()) {
            log.info(":::: cache is not empty");
            postRepository.bulkUpdateViewCountWithQueryDsl(allViewCountCache);
        }
    }
}
