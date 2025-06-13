package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardInspectionDefectRatioResponse {
    private Integer tornCount;
    private Integer stainCount;
    private Integer fadingCount;
    private Integer stretchedCount;
    private Integer otherCount;

    private Double tornRatio;
    private Double stainRatio;
    private Double fadingRatio;
    private Double stretchedRatio;
    private Double otherRatio;
}
