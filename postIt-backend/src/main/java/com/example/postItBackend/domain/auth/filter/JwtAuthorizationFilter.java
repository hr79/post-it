package com.example.postItBackend.domain.auth.filter;

import com.example.postItBackend.domain.auth.model.CustomUserDetails;
import com.example.postItBackend.domain.auth.service.UserDetailsServiceImpl;
import com.example.postItBackend.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// 인증 요청이 들어올 시
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // 인증 불필요 경로
    private final static Map<HttpMethod, Set<String>> whiteList = Map.of(
            HttpMethod.GET, Set.of(
                    "/", "/api/", "/api/auth/register**", "/api/login", "/api/board*", "/api/comment",
                    "/api/auth/oauth2-login*", "/api/auth/oauth2/callback", "/api/actuator/health", "/api/actuator/info", "/api/favicon.ico", "/favicon.ico"),
            HttpMethod.POST, Set.of(
                    "/api/auth/register*", "/api/login", "/api/auth/reissue", "/api/auth/oauth2-login*",
                    "/api/auth/oauth2/callback", "/api/actuator/health", "/api/actuator/info", "/api/favicon.ico", "/favicon.ico"
            ),
            HttpMethod.PATCH, Set.of(),
            HttpMethod.DELETE, Set.of(),
            HttpMethod.PUT, Set.of()
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("==== Authorization Filter ====");
        String requestURI = request.getRequestURI();
        System.out.println(requestURI);

        if (isWhiteListUrl(request)) {
            log.info(":::: 통과");
            chain.doFilter(request, response);
            return;
        }
        String header = Optional.ofNullable(request.getHeader("Authorization"))
                .orElseThrow(() -> new BadCredentialsException("JWT token is missing in the request header") {
                });
        log.info("header : {}", header);
        String accessToken = null;

        if (header.equals("Bearer null")) {
            throw new BadCredentialsException("JWT token is missing in the request header");
        }

        if (!header.startsWith("Bearer ")) {
            throw new BadCredentialsException("JWT token is missing in the request header");
        }
        accessToken = header.replace("Bearer ", "");

        if (accessToken.isEmpty()) {
            throw new BadCredentialsException("JWT token is missing in the request header");
        }


        if (jwtUtil.checkExpiredToken(accessToken)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            setAuthentication(accessToken);
            System.out.println(accessToken);
        }
        chain.doFilter(request, response);
    }

    private boolean isWhiteListUrl(HttpServletRequest request) {
        log.info(":::: isWhiteListUrl");
        HttpMethod methodType = HttpMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        log.info(":::: methodType = " + methodType);
        log.info(":::: requestURI = " + requestURI);

        if (whiteList.get(methodType).stream().anyMatch(uri -> PatternMatchUtils.simpleMatch(uri, requestURI))) {
            return true;
        }
        log.info("인증 필요 경로");
        return false;
    }

    private void setAuthentication(String accessToken) {
        Optional.of(accessToken)
                .map(token -> jwtUtil.getUsername(token))
//                .map(username -> userDetailsService.loadUserByUsername(username))
                .map(username -> new CustomUserDetails(username, null, null))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()))
                .ifPresent(authToken -> SecurityContextHolder.getContext().setAuthentication(authToken));
    }
}
