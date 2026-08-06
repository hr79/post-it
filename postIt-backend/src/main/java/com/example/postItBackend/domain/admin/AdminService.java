package com.example.postItBackend.domain.admin;

import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.comment.Comment;
import com.example.postItBackend.domain.comment.dto.CommentResponseDto;
import com.example.postItBackend.domain.comment.repository.CommentRepository;
import com.example.postItBackend.domain.enums.UserRole;
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
public class AdminService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // ==================== 사용자 관리 ====================

    /**
     * 모든 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * 특정 사용자 조회
     */
    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
    }

    /**
     * 사용자 역할 변경 (ADMIN/USER)
     */
    @Transactional
    public Member updateMemberRole(Long id, UserRole newRole) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Member updatedMember = Member.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .loginType(member.getLoginType())
                .role(newRole)
                .build();

        return memberRepository.save(updatedMember);
    }

    /**
     * 사용자 삭제 (본인 제외)
     */
    @Transactional
    public void deleteMember(Long id, String currentUsername) {
        Member currentUser = memberRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("현재 사용자를 찾을 수 없습니다"));

        if (currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("본인을 삭제할 수 없습니다");
        }

        memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        memberRepository.deleteById(id);
        log.info("User {} deleted by admin {}", id, currentUsername);
    }

    // ==================== 게시글 관리 ====================

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

    // ==================== 댓글 관리 ====================

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

    // ==================== 통계 ====================

    /**
     * 전체 사용자 수
     */
    @Transactional(readOnly = true)
    public long getMembersCount() {
        return memberRepository.count();
    }

    /**
     * 전체 게시글 수
     */
    @Transactional(readOnly = true)
    public long getPostsCount() {
        return postRepository.count();
    }

    /**
     * 전체 댓글 수
     */
    @Transactional(readOnly = true)
    public long getCommentsCount() {
        return commentRepository.count();
    }

    /**
     * ADMIN 사용자 수
     */
    @Transactional(readOnly = true)
    public long getAdminCount() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getRole() == UserRole.ADMIN)
                .count();
    }

    /**
     * USER 사용자 수
     */
    @Transactional(readOnly = true)
    public long getUserCount() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getRole() == UserRole.USER)
                .count();
    }
}

