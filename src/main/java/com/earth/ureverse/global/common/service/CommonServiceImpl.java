package com.earth.ureverse.global.common.service;

import com.earth.ureverse.global.common.dto.query.CategoryQueryDto;
import com.earth.ureverse.global.common.dto.response.BrandResponseDto;
import com.earth.ureverse.global.common.dto.response.CategoryListResponseDto;
import com.earth.ureverse.global.common.dto.response.SubCategoryResponseDto;
import com.earth.ureverse.global.mapper.BrandMapper;
import com.earth.ureverse.global.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public List<BrandResponseDto> getBrands() {
        List<BrandResponseDto> brandResponseDtos = brandMapper.selectAllBrands();
        return brandResponseDtos;
    }

    @Override
    public List<String> getCategories() {
        return categoryMapper.selectAllCategory();
    }

    @Override
    public List<CategoryListResponseDto> getCategoriesByBrandId(long brandId) {
        List<CategoryQueryDto> categoryResponseDtos = categoryMapper.selectCategoriesByBrandId(brandId);

        List<CategoryListResponseDto> categoryList = categoryResponseDtos.stream()
                .collect(Collectors.groupingBy(CategoryQueryDto::getMainName))
                .entrySet().stream()
                .map(entry -> {
                    String mainName = entry.getKey();
                    List<SubCategoryResponseDto> subList = entry.getValue().stream()
                            .map(dto -> new SubCategoryResponseDto(dto.getCategoryId(), dto.getSubName(), dto.getPoint()))
                            .collect(Collectors.toList());

                    return new CategoryListResponseDto(mainName, subList);
                })
                .collect(Collectors.toList());

        return categoryList;
    }
}
