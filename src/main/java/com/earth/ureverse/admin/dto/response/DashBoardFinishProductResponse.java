package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardFinishProductResponse {
    private String baseDate; //일자 또는 월(2025-06-13 / 2025-06)
    private Integer finishCount; //수거 완료 건수
}
