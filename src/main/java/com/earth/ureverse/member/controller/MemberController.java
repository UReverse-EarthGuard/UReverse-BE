package com.earth.ureverse.member.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping
    public CommonResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody WithdrawRequestDto dto,
            HttpServletResponse response
    ) {
        memberService.withdraw(customUserDetails.getUserId(), dto);

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
