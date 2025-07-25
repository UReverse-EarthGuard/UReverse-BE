package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.auth.dto.response.KakaoUserInfo;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.admin-key}")
    private String adminKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMapper authMapper;
    private final WebClient webClient;

    // 카카오 로그인 URL 생성
    public String getKakaoLoginUrl(String encodedState) {
        return "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoRestApiKey
                + "&redirect_uri=" + kakaoRedirectUri
                + "&response_type=code"
                + "&scope=profile_nickname,talk_message"
                + "&state=" + encodedState;
    }

    // 카카오 사용자 정보 조회 및 연동 처리
    public KakaoUserInfo kakaoLogin(String code, String jwt) {

        Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
        authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String accessToken = getKakaoAccessToken(code);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        authMapper.linkKakaoAccount(userId, kakaoUserInfo);

        return kakaoUserInfo;
    }

    // 토큰 요청
    private String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoRestApiKey);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        return json.getString("access_token");
    }

    // 사용자 정보 요청
    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me?property_keys=[\"kakao_account.profile\"]",
                HttpMethod.GET,
                request,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        Long kakaoId = json.getLong("id");
        JSONObject account = json.getJSONObject("kakao_account");
        JSONObject profile = account.getJSONObject("profile");
        String nickname = profile.optString("nickname", null);

        return new KakaoUserInfo(kakaoId, nickname);
    }

    public void unlinkKakao(String kakaoUserId) {
        try {
            String kakaoUnlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

            webClient.post()
                    .uri(kakaoUnlinkUrl)
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + adminKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(BodyInserters.fromFormData("target_id_type", "user_id")
                            .with("target_id", kakaoUserId))
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), response ->
                            response.bodyToMono(String.class)
                                    .doOnNext(errorBody -> log.warn("카카오 탈퇴 실패 응답: {}", errorBody))
                                    .flatMap(errorBody -> Mono.error(new RuntimeException("카카오 탈퇴 실패: " + errorBody))))
                    .bodyToMono(String.class)
                    .doOnNext(responseBody -> log.info("카카오 탈퇴 성공: {}", responseBody))
                    .block();
        } catch (Exception ex) {
            log.error("카카오 탈퇴 중 예외 발생", ex);
        }
    }
}
