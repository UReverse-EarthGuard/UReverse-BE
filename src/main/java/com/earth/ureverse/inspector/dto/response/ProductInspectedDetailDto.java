package com.earth.ureverse.inspector.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductInspectedDetailDto {
    private Long productId;
    private List<String> imageUrls;
    private String brandName;
    private String categoryMain;
    private String categorySub;
    private Long paidPoint;
    private Long expectedPoint;
    private String inspectedAt;
    private InspectionResultDto aiInspection;
    private InspectionResultDto humanInspection;
}

