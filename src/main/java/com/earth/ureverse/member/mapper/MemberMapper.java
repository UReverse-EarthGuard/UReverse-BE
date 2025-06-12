package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    void updateIsActive(@Param("userId") Long userId, @Param("isActive") String isActive);

    void updateMember(@Param("userId") Long userId,
                      @Param("updateMemberRequestDto") UpdateMemberRequestDto updateMemberRequestDto);

}
