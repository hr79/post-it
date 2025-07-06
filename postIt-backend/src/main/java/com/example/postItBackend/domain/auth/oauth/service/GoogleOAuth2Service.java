package com.example.postItBackend.domain.auth.oauth.service;

import com.example.postItBackend.domain.auth.model.CustomUserDetails;
import com.example.postItBackend.domain.auth.model.Member;
import com.example.postItBackend.domain.auth.MemberRepository;
import com.example.postItBackend.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Service implements AuthService {
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;


    @Transactional
    public String getOAuth2Url() {
        String authorizationEndPoint = "https://accounts.google.com/o/oauth2/v2/auth";
        String authorizationUri = authorizationEndPoint
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=openid%20profile%20email";
        log.info("authorizationUri: {}", authorizationUri);

        return authorizationUri;
    }

    @Transactional
    public String handleOAuthCallback(String code, HttpServletResponse response) {
        log.info(":::#1 handleOAuthCallback:::");
        String GoogleOAuthAccessToken = Optional.of(code)
                .map(c -> getAccessToken(c))
                .orElseThrow(() -> new IllegalArgumentException("google oauth accessToken is null"));

        ResponseEntity<Map> googleUserInfo = getGoogleUserInfo(GoogleOAuthAccessToken);
        log.info("googleUserInfo: {}", googleUserInfo);
        Map body = googleUserInfo.getBody();

        log.info("body: {}", body);
        log.info("body.get(\"email\"): {}", body.get("email"));
        log.info("body.get(\"name\"): {}", body.get("name"));
        log.info("body.get(\"picture\"): {}", body.get("picture"));
        log.info("body.get(\"locale\"): {}", body.get("locale"));
        log.info("body.get(\"sub\"): {}", body.get("sub"));
        log.info("body.get(\"hd\"): {}", body.get("hd"));
        log.info("body.get(\"email_verified\"): {}", body.get("email_verified"));
        log.info("body.get(\"gender\"): {}", body.get("gender"));
        log.info("body.get(\"given_name\"): {}", body.get("given_name"));

        Member member = Optional.ofNullable(((String) body.get("email")))
                .flatMap(memberRepository::findByEmail)
                .orElseGet(() -> saveMember(body));
        log.info("member: {}", member);

//        return setToken(response, member.getUsername());
        String accessToken = setToken(response, member.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return accessToken;
    }

    private String setToken(HttpServletResponse response, String username) {
        String accessToken = jwtUtil.generateAccessToken(username);
        log.info("#5 New Access Token: {}", accessToken);

        CustomUserDetails userDetails = new CustomUserDetails(username, null, null);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        response.setHeader("Authorization", "Bearer " + accessToken);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
        return accessToken;
    }

    private ResponseEntity<Map> getGoogleUserInfo(String accessToken) {
        log.info(":::#3 getGoogleUserInfo:::");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        ResponseEntity<Map> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class);
        log.info("response: {}", response.getBody());

        return response;
    }


    private String getAccessToken(String code) {
        log.info(":::#2 getAccessToken:::");
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<Object, Object> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<LinkedMultiValueMap<Object, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        log.info("response: {}", response.getBody());

        return response.getBody().get("access_token").toString();
    }

    private Member saveMember(Map body) {
        log.info(":::#4 saveMember:::");
        Member member = Member.builder()
                .username((String) body.get("email"))
                .nickname((String) body.get("name"))
                .email((String) body.get("email"))
                .loginType("google")
                .build();
        memberRepository.save(member);

        return member;
    }
}
