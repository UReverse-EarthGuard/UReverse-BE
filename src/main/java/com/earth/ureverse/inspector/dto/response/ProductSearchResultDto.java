package com.earth.ureverse.inspector.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchResultDto {
    private Long productId;
    private String brandName;
    private String categoryMain;
    private String categorySub;
    private String status;
    private String imageUrl; // 대표 이미지 1개
    private String createdAt;
    private Integer expectedPoint;
}
