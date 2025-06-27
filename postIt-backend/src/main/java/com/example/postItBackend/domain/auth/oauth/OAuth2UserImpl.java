package com.example.postItBackend.domain.auth.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class OAuth2UserImpl implements OAuth2User {
    private String email;
    private String name;
    private Map<String, Object> attributes;

    @Builder
    public OAuth2UserImpl(String email, String name, Map<String, Object> attributes) {
        this.email = email;
        this.name = name;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return name;
    }

}
