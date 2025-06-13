package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.response.PointHistoryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointMapper {

    List<PointHistoryResponseDto> getPointHistory(
            @Param(value = "userId") Long userId,
            @Param(value = "limit") int limit,
            @Param(value = "lastCreatedAt") String lastCreatedAt,
            @Param(value = "lastProductId") Long lastProductId
    );

    int getTotalPoint(@Param(value = "userId") Long userId);

}
