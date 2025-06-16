package com.earth.ureverse.global.common.mapper;

import com.earth.ureverse.global.common.dto.query.CategoryQueryDto;
import com.earth.ureverse.global.common.dto.response.CategoryListResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<String> selectAllCategory();

    List<CategoryQueryDto> selectCategoriesByBrandId(Long brandId);
}
