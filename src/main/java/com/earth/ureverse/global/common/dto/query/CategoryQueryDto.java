package com.earth.ureverse.global.common.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryQueryDto {
    private Long categoryId;
    private String mainName;
    private String subName;
    private Long point;
}