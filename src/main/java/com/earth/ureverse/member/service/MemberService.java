package com.earth.ureverse.member.service;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.member.dto.request.ChangePasswordRequestDto;
import com.earth.ureverse.member.dto.request.ProductUploadRequestDto;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.dto.response.MemberInfoResponseDto;
import com.earth.ureverse.member.dto.response.PointHistoryListResponseDto;
import com.earth.ureverse.member.dto.response.SalesHistoryListResponseDto;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    void withdraw(Long userId, WithdrawRequestDto withdrawRequestDto);

    void updateMember(Long userId, @Valid UpdateMemberRequestDto updateMemberRequestDto);

    void changePassword(Long userId, @Valid ChangePasswordRequestDto changePasswordRequestDto);

    PointHistoryListResponseDto getPointHistory(Long userId, int limit, String lastCreatedAt, Long lastProductId);

    SalesHistoryListResponseDto getSalesHistory(Long userId, int limit, String lastCreatedAt, Long lastProductId);

    MemberInfoResponseDto getMyInfo(Long userId);

    void registerProduct(ProductUploadRequestDto dto, Long userId) ;

    List<NotificationDto> getNotifications(Long userId);

    void markNotificationsAsRead(Long userId, List<Long> notificationIdList);
}
