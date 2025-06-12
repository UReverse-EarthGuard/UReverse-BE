package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInspectionResultResponse {
    private ProductDetailResponse product;
    private InspectionResultResponse aiResult;
    private InspectionResultResponse inspectorResult;
    private String grade;
}
