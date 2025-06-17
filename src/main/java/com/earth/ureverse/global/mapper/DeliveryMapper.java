package com.earth.ureverse.global.mapper;

import com.earth.ureverse.member.dto.request.ProductUploadRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeliveryMapper {
    void insertDelivery(@Param("productId") Long productId,
                        @Param("userId") Long userId,
                        @Param("dto") ProductUploadRequestDto dto);
}
