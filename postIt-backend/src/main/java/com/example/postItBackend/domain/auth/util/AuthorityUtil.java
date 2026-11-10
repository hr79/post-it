package com.example.postItBackend.domain.auth.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.postItBackend.domain.enums.UserRole;

import java.util.List;

public class AuthorityUtil {
    public static List<GrantedAuthority> createAuthorities(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }
}

