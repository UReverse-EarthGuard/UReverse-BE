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

    public double getPassRatio(){
        double result = totalCount != null && totalCount>0
                        ? Math.round((double) passCount / totalCount * 10000) / 100.0
                        : 0.0;
        return result;
    }
    public double getFailRatio(){
        double result = totalCount != null && totalCount>0
                ? Math.round((double) failCount / totalCount * 10000) / 100.0
                : 0.0;
        return result;
    }
}
