package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    void updateIsActive(@Param("userId") Long userId, @Param("isActive") String isActive);

    void updateMember(@Param("userId") Long userId,
                      @Param("updateMemberRequestDto") UpdateMemberRequestDto updateMemberRequestDto);

    void updatePassword(@Param("userId") Long userId, @Param("encodedPassword") String encodedPassword);

    List<ActiveMemberResponse> getActiveUsers(ActiveMemberSearchRequest request);

    long countActiveUsers(ActiveMemberSearchRequest request);
}
