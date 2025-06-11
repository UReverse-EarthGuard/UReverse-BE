package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinishProductResponse {
    private int id; //productId
    private String image; //대표 이미지
    private String brand; //브랜드명
    private String category; //카테고리명
    private String grade; //상품등급
    private String finishDate; //수거 완료 날짜(updated_at)
}
