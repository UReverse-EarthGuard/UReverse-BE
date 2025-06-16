package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.response.BrandResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {

    List<BrandResponseDto> selectAllBrands();
}
