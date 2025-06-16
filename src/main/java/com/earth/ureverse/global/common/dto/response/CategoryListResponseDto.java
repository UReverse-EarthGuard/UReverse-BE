package com.earth.ureverse.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponseDto {
    private String mainCategoryName;
    private List<SubCategoryResponseDto> subCategoryResponseDtoList;
}
