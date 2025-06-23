package com.example.postItBackend.service;

import com.example.postItBackend.dto.CommentRequestDto;
import com.example.postItBackend.dto.CommentResponseDto;
import com.example.postItBackend.model.Comment;
import com.example.postItBackend.model.Member;
import com.example.postItBackend.model.Post;
import com.example.postItBackend.repository.CommentRepository;
import com.example.postItBackend.repository.MemberRepository;
import com.example.postItBackend.repository.PostRepository;
import com.example.postItBackend.util.CacheUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CrudService<Comment, Long> crudService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CrudService<Post, Long> postCrudService;
    private final CacheUtil cacheUtil;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository1, MemberRepository memberRepository, PostRepository postRepository, CacheUtil cacheUtil) {
        this.crudService = new CrudService<>(commentRepository);
        this.postRepository = postRepository1;
        this.memberRepository = memberRepository;
        this.postCrudService = new CrudService<>(postRepository);
        this.commentRepository = commentRepository;
        this.cacheUtil = cacheUtil;
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsInPost(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        return commentList.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(), comment.getContent(), comment.getMember().getNickname(), postId))
                .toList();

    }

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, UserDetails userDetails) {
        Member member = cacheUtil.findByUsernameWithCache(userDetails.getUsername());
        Comment comment = Comment.builder()
                .content(requestDto.getContent())
//                .postId(postId)
                .member(member)
                .build();

        crudService.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto createCommentVer2(Long postId, CommentRequestDto requestDto, UserDetails userDetails) {
        Member member = cacheUtil.findByUsernameWithCache(userDetails.getUsername());
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId, UserDetails userDetails, Long boardId) {
        Comment comment = crudService.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Optional.of(comment)
                .map(Comment::getMember)
                .map(Member::getUsername)
                .map(u -> u.equals(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("You do not have permission to delete this comment"));

        crudService.deleteById(commentId);
        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(UserDetails userDetails) {
        List<Comment> all = crudService.findAll();
        return all.stream().map(comment -> new CommentResponseDto(comment)).toList();
    }
}
