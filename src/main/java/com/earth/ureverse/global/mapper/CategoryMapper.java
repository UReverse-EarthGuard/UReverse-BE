package com.earth.ureverse.global.mapper;

import com.earth.ureverse.global.common.dto.query.CategoryQueryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<String> selectAllCategory();

    List<CategoryQueryDto> selectCategoriesByBrandId(Long brandId);
}
