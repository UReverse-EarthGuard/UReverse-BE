package com.earth.ureverse.member.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping
    public CommonResponseEntity<Void> withdraw(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody WithdrawRequestDto withdrawRequestDto,
            HttpServletResponse response
    ) {
        memberService.withdraw(customUserDetails.getUserId(), withdrawRequestDto);

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

    @PatchMapping
    public CommonResponseEntity<Void> updateMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdateMemberRequestDto updateMemberRequestDto
    ) {
        memberService.updateMember(customUserDetails.getUserId(), updateMemberRequestDto);
        return CommonResponseEntity.success(null);
    }

}
