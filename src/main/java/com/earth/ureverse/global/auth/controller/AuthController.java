package com.earth.ureverse.global.auth.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.request.RecoveryPasswordRequestDto;
import com.earth.ureverse.global.auth.dto.request.SignUpRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;
import com.earth.ureverse.global.auth.service.AuthService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Tag(name = "Auth", description = "인증/인가 관리 서비스 API 입니다.")
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
    public CommonResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
        return CommonResponseEntity.success("로그아웃되었습니다.");
    }

    @PostMapping("/sign-up")
    public CommonResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return CommonResponseEntity.success("회원가입이 완료되었습니다.");
    }

    //이름 출력
    @GetMapping("/name")
    public CommonResponseEntity<String> getUserName(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return CommonResponseEntity.success(authService.getUserName(customUserDetails));
    }

    @PostMapping("/recovery/password")
    public CommonResponseEntity<String> recoverPassword(@RequestBody @Valid RecoveryPasswordRequestDto recoveryPasswordRequestDto) {
        authService.recoverPassword(recoveryPasswordRequestDto);
        return CommonResponseEntity.success("임시 비밀번호가 이메일로 전송되었습니다.");
    }

}
