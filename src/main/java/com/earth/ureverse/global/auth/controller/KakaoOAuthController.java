package com.earth.ureverse.global.auth.controller;

import com.earth.ureverse.global.auth.dto.response.KakaoUserInfo;
import com.earth.ureverse.global.auth.service.KakaoOauthService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class KakaoOAuthController {

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/kakao")
    public void redirectToKakaoAuth(@RequestParam("token") String accessToken,
                                    HttpServletResponse response) throws IOException {
        String redirectUrl = kakaoOauthService.getKakaoLoginUrl(accessToken);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/kakao/callback")
    public CommonResponseEntity<KakaoUserInfo> kakaoCallback(@RequestParam("code") String code,
                                                             @RequestParam("state") String accessToken ) {
        return CommonResponseEntity.success(kakaoOauthService.kakaoLogin(code, accessToken));
    }
}
