package com.earth.ureverse.inspector.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductInspectionDetailDto {
    private Long productId;
    private List<String> imageUrls;
    private String brandName;
    private String categoryMain;
    private String categorySub;
    private Long expectedPoint;
    private Long userId;
    private InspectionResultDto aiInspection;
}
