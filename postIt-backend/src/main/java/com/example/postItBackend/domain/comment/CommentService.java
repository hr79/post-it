package com.example.postItBackend.domain.comment;

import com.example.postItBackend.domain.comment.dto.CommentRequestDto;
import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.post.Post;
import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.domain.post.repository.PostRepository;
import com.example.postItBackend.common.util.CacheUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CacheUtil cacheUtil;

    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository, PostRepository postRepository, CacheUtil cacheUtil) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = Comment.builder().content(requestDto.getContent()).member(member).post(post).build();
        post.increaseCommentCount();

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId, UserDetails userDetails, Long boardId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Optional.of(comment)
                .map(Comment::getMember)
                .map(Member::getUsername)
                .map(u -> u.equals(userDetails.getUsername()))
                .orElseThrow(() -> new IllegalArgumentException("You do not have permission to delete this comment"));

        commentRepository.deleteById(commentId);
        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllComments(UserDetails userDetails) {
        List<Comment> all = commentRepository.findAll();
        return all.stream().map(comment -> new CommentResponseDto(comment)).toList();
    }
}
