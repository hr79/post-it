package com.example.postItBackend.domain.auth.service;

import com.example.postItBackend.domain.auth.model.CustomUserDetails;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.common.util.CacheUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CacheUtil cacheUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("====== this is loadUserByUsername ======");
//        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorMessages.USER_NOT_FOUND));
        Member member = cacheUtil.findByUsernameWithCache(username);
        return new CustomUserDetails(member.getUsername(), member.getPassword(), null);
    }
}
