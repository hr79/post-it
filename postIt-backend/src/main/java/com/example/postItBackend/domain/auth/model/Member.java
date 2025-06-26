package com.example.postItBackend.domain.auth.model;

import com.example.postItBackend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member", indexes = @Index(name = "idx_username", columnList = "username", unique = true))
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String loginType;

    @Column(nullable = true)
    private String providerId;

    @Builder
    public Member(Long id, String username, String nickname, String email, String password, String loginType) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }
}
