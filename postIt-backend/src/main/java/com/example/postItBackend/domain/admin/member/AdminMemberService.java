package com.example.postItBackend.domain.admin.member;

import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

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

    /**
     * 전체 사용자 수
     */
    @Transactional(readOnly = true)
    public long getMembersCount() {
        return memberRepository.count();
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

