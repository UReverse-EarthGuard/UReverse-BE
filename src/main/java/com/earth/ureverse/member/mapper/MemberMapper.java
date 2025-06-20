package com.earth.ureverse.member.mapper;

import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import com.earth.ureverse.member.dto.response.MemberInfoResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

    void updateIsActive(@Param("userId") Long userId, @Param("isActive") String isActive);

    void updateMember(@Param("userId") Long userId,
                      @Param("updateMemberRequestDto") UpdateMemberRequestDto updateMemberRequestDto);

    void updatePassword(@Param("userId") Long userId, @Param("encodedPassword") String encodedPassword);

    List<ActiveMemberResponse> getActiveUsers(ActiveMemberSearchRequest request);

    long countActiveUsers(ActiveMemberSearchRequest request);

    MemberInfoResponseDto findMyInfoByUserId(@Param("userId") Long userId);

    List<Map<String, Object>> countProductStatus(@Param("userId") Long userId);

    List<NotificationDto> findNotificationsByUserId(Long userId);

    void updateNotificationIsRead(@Param("userId") Long userId, @Param("ids") List<Long> ids);

    String getEmailByUserId(@Param("userId") Long userId);
}
