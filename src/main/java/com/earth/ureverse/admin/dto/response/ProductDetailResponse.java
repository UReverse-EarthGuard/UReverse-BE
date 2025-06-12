package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private Long id; //productId
    private String image1;
    private String image2;
    private String image3;
    private String brand; //브랜드명
    private String category; //카테고리명
    private Integer paid_point;
    private Integer expect_point;
    private String userEmail;
    private String status; //상품 현황
    private String updatedAt; //완료 날짜(updated_at)
}
