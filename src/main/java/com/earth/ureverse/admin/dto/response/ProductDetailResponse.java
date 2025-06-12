package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private Long id; //productId
    private List<String> images;
    private String brand; //브랜드명
    private String categoryMain; //카테고리 Main
    private String categorySub; //카테고리 Sub
    private Integer paidPoint;
    private Integer expectPoint;
    private String userEmail;
    private String status; //상품 현황
    private String updatedAt; //완료 날짜(updated_at)
}
