package com.earth.ureverse.global.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private Long kakaoId;
    private String nickname;
}
