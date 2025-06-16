package com.earth.ureverse.global.mapper;

import com.earth.ureverse.global.common.dto.response.BrandResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<BrandResponseDto> selectAllBrands();
}
