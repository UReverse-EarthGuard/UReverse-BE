package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardBrandResponse {
    private String name; //브랜드 이름
    private Integer salesCount; //판매 건수
}
