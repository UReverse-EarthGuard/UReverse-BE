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
public class DashBoardSummaryResponse {
    private Integer pickupRequest; //수거요청대기건수
    private Long totalPaidPoint; //지급포인트 총합
    private List<DashBoardBrandResponse> topBrands;
}
