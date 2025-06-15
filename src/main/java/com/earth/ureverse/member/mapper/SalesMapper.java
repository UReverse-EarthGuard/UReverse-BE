package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.response.SalesHistoryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesMapper {

    List<SalesHistoryResponseDto> getSalesHistory(
            @Param(value = "userId") Long userId,
            @Param(value = "limit") int limit,
            @Param(value = "lastCreatedAt") String lastCreatedAt,
            @Param(value = "lastProductId") Long lastProductId
    );

}
