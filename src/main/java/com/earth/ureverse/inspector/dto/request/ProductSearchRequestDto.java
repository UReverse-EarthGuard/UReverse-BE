package com.earth.ureverse.inspector.dto.request;

import lombok.Data;

@Data
public class ProductSearchRequestDto {
    private boolean inspected;
    private String keyword;       // 상품명/브랜드명 검색어
}
