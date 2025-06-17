package com.earth.ureverse.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponseDto {
    private Long categoryId;
    private String name;
    private Long point;
}
