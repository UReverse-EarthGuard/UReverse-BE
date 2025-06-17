package com.earth.ureverse.global.auth.controller;

import com.earth.ureverse.global.auth.dto.response.KakaoUserInfo;
import com.earth.ureverse.global.auth.service.KakaoOauthService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class KakaoOAuthController {

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/kakao")
    public void redirectToKakaoAuth(@RequestParam("state") String encodedState,
                                    HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoOauthService.getKakaoLoginUrl(encodedState);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<Void> kakaoCallback(@RequestParam("code") String code,
                              @RequestParam("state") String encodedState,
                              HttpServletResponse response) throws IOException {
        // state 파싱
        String jsonString = new String(Base64.getDecoder().decode(encodedState), StandardCharsets.UTF_8);
        JSONObject stateJson = new JSONObject(jsonString);

        String jwt = stateJson.getString("jwt");
        String frontendRedirectUri = stateJson.getString("redirectUri");

        KakaoUserInfo result = kakaoOauthService.kakaoLogin(code, jwt);
        String encodedNickname = URLEncoder.encode(result.getNickname(), StandardCharsets.UTF_8);

        // 결과를 redirect uri에 붙여서 프론트로 전달
        String finalRedirect = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("success", true)
                .queryParam("kakaoId", result.getKakaoId())
                .queryParam("nickname", encodedNickname)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(finalRedirect));

        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
    }
}
