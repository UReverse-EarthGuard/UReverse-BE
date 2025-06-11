package com.earth.ureverse.global.auth.controller;

import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;
import com.earth.ureverse.global.auth.service.AuthService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public CommonResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponse = authService.login(loginRequestDto);

        // HttpOnly 쿠키로 refreshToken 내려주기
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(14))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // refreshToken은 바디에 포함하지 않고, accessToken과 role만 응답
        return CommonResponseEntity.success(
                new LoginResponseDto(loginResponse.getAccessToken(), null, loginResponse.getRole())
        );
    }

    @GetMapping("/refresh")
    public CommonResponseEntity<LoginResponseDto> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        return CommonResponseEntity.success(authService.refreshAccessToken(refreshToken));
    }

    @PostMapping("/logout")
    public CommonResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        return CommonResponseEntity.success(null);
    }

}
