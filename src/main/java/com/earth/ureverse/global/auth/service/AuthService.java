package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.request.RecoveryPasswordRequestDto;
import com.earth.ureverse.global.auth.dto.request.SignUpRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto refreshAccessToken(String refreshToken);

    void signUp(SignUpRequestDto signUpRequestDto);

    String getUserName(CustomUserDetails customUserDetails);

    void recoverPassword(RecoveryPasswordRequestDto recoveryPasswordRequestDto);

}
