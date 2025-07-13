package com.example.postItBackend.config;

import com.example.postItBackend.domain.auth.service.AuthenticationManagerImpl;
import com.example.postItBackend.domain.auth.filter.JwtAuthenticationFilter;
import com.example.postItBackend.domain.auth.filter.JwtAuthorizationFilter;
import com.example.postItBackend.domain.auth.service.RedisService;
import com.example.postItBackend.domain.auth.service.UserDetailsServiceImpl;
import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.domain.auth.oauth.service.GoogleOAuth2Service;
import com.example.postItBackend.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final UserDetailsServiceImpl userDetailsService;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager, jwtUtil, redisService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        JwtAuthorizationFilter jwtAuthorizationFilter =
                new JwtAuthorizationFilter(jwtUtil, userDetailsService);
        GoogleOAuth2Service googleOAuth2Service = new GoogleOAuth2Service(memberRepository, restTemplate, jwtUtil);

        http
                .cors(Customizer.withDefaults()) // cors 허용
                .csrf(AbstractHttpConfigurer::disable) // 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 경로병 권한 설정
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/register/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/reissue").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/oauth2-login").permitAll()
                                .requestMatchers("/api/auth/oauth2/callback").permitAll()
                                .requestMatchers("/favicon.ico").permitAll()
//                        .requestMatchers(HttpMethod.GET, "**/*.ico").permitAll()
                                .requestMatchers(HttpMethod.GET, "/board*").permitAll()
                                .anyRequest().authenticated() // 그 외의 경로는 모두 인증이 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class); // jwtAuth~ 필터를 Username~ 필터 앞에, 요청이 들어올 때마다 토큰 확인 후 인증된 상태로 처리

        http.formLogin(formLogin -> formLogin.disable()); //formLogin 허용 시 JwtAuthenticationFilter가 아닌 스프링시큐리티 UsernamePasswordAuthenticationFilter 기본필터로 로그인이 처리됨. 그래서 필터가 동작하지 않음.

//        http.oauth2Login(oauth2 -> oauth2
//                .loginPage("/oauth2-login")
//                .defaultSuccessUrl("/success", false)
//                .failureUrl("/login?error=true")
//                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)
//                ));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManagerImpl(userDetailsService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8081",
                "http://13.209.85.84",
                "https://post-it-service.shop"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // 쿠키 인증 허용 (필요 시 활성화)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }
}

