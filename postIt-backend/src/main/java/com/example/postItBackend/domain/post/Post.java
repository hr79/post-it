package com.example.postItBackend.domain.post;

import com.example.postItBackend.domain.BaseEntity;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.viewCount = 0;
        this.commentCount = 0;
    }

    public Post update(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }
}
