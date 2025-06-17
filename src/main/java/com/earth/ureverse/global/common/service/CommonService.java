package com.earth.ureverse.global.common.service;

import com.earth.ureverse.global.common.dto.response.BrandResponseDto;
import com.earth.ureverse.global.common.dto.response.CategoryListResponseDto;

import java.util.List;

public interface CommonService {

    List<BrandResponseDto> getBrands();

    List<String> getCategories();

    List<CategoryListResponseDto> getCategoriesByBrandId(long brandId);
}
