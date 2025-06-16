package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.request.RecoveryPasswordRequestDto;
import com.earth.ureverse.global.auth.dto.request.SignUpRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;
import jakarta.validation.Valid;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto refreshAccessToken(String refreshToken);

    void signUp(SignUpRequestDto signUpRequestDto);

    String getUserName(CustomUserDetails customUserDetails);

    void recoverPassword(@Valid RecoveryPasswordRequestDto recoveryPasswordRequestDto);

}
