package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardInspectionResultRatioResponse {
    private Integer totalCount;
    private Integer passCount; //통과 건수
    private Integer failCount; //탈락 건수
    private Double passRatio;
    private Double failRatio;
}
