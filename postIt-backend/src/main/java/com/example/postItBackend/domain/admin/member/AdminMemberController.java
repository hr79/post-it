package com.example.postItBackend.domain.admin.member;

import com.example.postItBackend.common.response.ApiResponse;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    /**
     * 모든 사용자 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllMembers() {
        return ResponseEntity.ok(ApiResponse.success(adminMemberService.getAllMembers(), HttpStatus.OK.value(), "모든 사용자 조회 성공"));
    }

    /**
     * 특정 사용자 조회
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        Member member = adminMemberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(member, HttpStatus.OK.value(), "사용자 조회 성공"));
    }

    /**
     * 사용자 권한 변경 (ADMIN/USER)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateMemberRole(@PathVariable Long id, @RequestBody RoleUpdateRequest roleRequest) {
        try {
            UserRole newRole = UserRole.valueOf(roleRequest.getRole().toUpperCase());
            Member member = adminMemberService.updateMemberRole(id, newRole);
            return ResponseEntity.ok(ApiResponse.success(member, HttpStatus.OK.value(), "사용자 권한이 변경되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, "유효하지 않은 역할입니다"));
        }
    }

    /**
     * 사용자 삭제
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            adminMemberService.deleteMember(id, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.OK.value(), "사용자가 삭제되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("", 400, e.getMessage()));
        }
    }

    /**
     * 통계: 전체 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/count")
    public ResponseEntity<?> getMembersCount() {
        long count = adminMemberService.getMembersCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "전체 사용자 수 조회 성공"));
    }

    /**
     * 통계: ADMIN 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/admin-count")
    public ResponseEntity<?> getAdminCount() {
        long count = adminMemberService.getAdminCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "ADMIN 사용자 수 조회 성공"));
    }

    /**
     * 통계: USER 사용자 수
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/user-count")
    public ResponseEntity<?> getUserCount() {
        long count = adminMemberService.getUserCount();
        return ResponseEntity.ok(ApiResponse.success(count, HttpStatus.OK.value(), "USER 사용자 수 조회 성공"));
    }

    /**
     * 권한 변경 요청 DTO
     */
    @Getter
    @Setter
    public static class RoleUpdateRequest {
        private String role;
    }
}

