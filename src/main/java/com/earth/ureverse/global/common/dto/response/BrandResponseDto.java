package com.earth.ureverse.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDto {
    private int brandId;
    private String name;
    private String nameEn;
    private String logo;
}