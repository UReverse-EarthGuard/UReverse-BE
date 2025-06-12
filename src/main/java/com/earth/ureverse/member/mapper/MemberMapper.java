package com.earth.ureverse.member.mapper;

import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    void updateIsActive(@Param("userId") Long userId, @Param("isActive") String isActive);

    List<ActiveMemberResponse> getActiveUsers(ActiveMemberSearchRequest request);

    long countActiveUsers(ActiveMemberSearchRequest request);
}
