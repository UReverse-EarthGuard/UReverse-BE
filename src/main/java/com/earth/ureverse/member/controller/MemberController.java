package com.earth.ureverse.member.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.member.dto.request.ChangePasswordRequestDto;
import com.earth.ureverse.member.dto.request.ProductUploadRequestDto;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.dto.response.MemberInfoResponseDto;
import com.earth.ureverse.member.dto.response.PointHistoryListResponseDto;
import com.earth.ureverse.member.dto.response.SalesHistoryListResponseDto;
import com.earth.ureverse.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Member", description = "고객 서비스 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping
    public CommonResponseEntity<String> withdraw(
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

        return CommonResponseEntity.success("탈퇴 처리되었습니다.");
    }

    @PatchMapping
    public CommonResponseEntity<String> updateMember(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody UpdateMemberRequestDto updateMemberRequestDto
    ) {
        memberService.updateMember(customUserDetails.getUserId(), updateMemberRequestDto);
        return CommonResponseEntity.success("회원 정보가 수정되었습니다.");
    }

    @PatchMapping("/password")
    public CommonResponseEntity<String> changePassword(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto
    ) {
        memberService.changePassword(customUserDetails.getUserId(), changePasswordRequestDto);
        return CommonResponseEntity.success("비밀번호가 변경되었습니다.");
    }

    @GetMapping("/points")
    public CommonResponseEntity<PointHistoryListResponseDto> getPointHistories(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String lastCreatedAt,
            @RequestParam(required = false) Long lastProductId
    ) {
        PointHistoryListResponseDto pointHistoryListResponseDto = memberService.getPointHistory(
                customUserDetails.getUserId(), limit, lastCreatedAt, lastProductId
        );
        return  CommonResponseEntity.success(pointHistoryListResponseDto);
    }

    @GetMapping("/sales")
    public CommonResponseEntity<SalesHistoryListResponseDto> getSalesHistory(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String lastCreatedAt,
            @RequestParam(required = false) Long lastProductId
    ) {
        SalesHistoryListResponseDto salesHistoryListResponseDto = memberService.getSalesHistory(
                customUserDetails.getUserId(), limit, lastCreatedAt, lastProductId
        );
        return CommonResponseEntity.success(salesHistoryListResponseDto);
    }

    @GetMapping("/me")
    public CommonResponseEntity<MemberInfoResponseDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.getMyInfo(customUserDetails.getUserId());
        return CommonResponseEntity.success(memberInfoResponseDto);
    }

    @PostMapping("/product")
    public CommonResponseEntity<String> addProduct(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody ProductUploadRequestDto dto
    ) {

        memberService.registerProduct(dto, customUserDetails.getUserId());

        return CommonResponseEntity.success("상품 등록을 성공했습니다.");
    }

    @GetMapping("/notifications")
    public CommonResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<NotificationDto> notificationDtoList = memberService.getNotifications(customUserDetails.getUserId());
        return CommonResponseEntity.success(notificationDtoList);
    }

    @PutMapping("/readNotification")
    public CommonResponseEntity<String> readNotification(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody List<Long> notificationIdList
            ){
        Long userId = customUserDetails.getUserId();
        memberService.markNotificationsAsRead(userId, notificationIdList);
        return CommonResponseEntity.success("알림 읽음 처리 완료");
    }
}
