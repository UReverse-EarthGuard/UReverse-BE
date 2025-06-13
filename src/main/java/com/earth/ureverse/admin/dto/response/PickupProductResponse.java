package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PickupProductResponse {
    private Long id; //productId
    private String image; //대표 이미지
    private String brand; //브랜드명
    private String categoryMain; //카테고리 Main
    private String categorySub; //카테고리 Sub
    private String grade; //상품등급
    private Integer paid_point; // 지급 포인트
    private String status; //수거 현황
    private String updatedAt; //수거 완료 날짜(updated_at)
}
