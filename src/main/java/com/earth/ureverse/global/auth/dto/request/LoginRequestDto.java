package com.earth.ureverse.global.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    private String email;    // 로그인 시, 사용하는 ID
    private String password;

}
