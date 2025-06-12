package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InspectionResultResponse {
    private String inspectorEmail;
    private Boolean isTorn;
    private Boolean hasStain;
    private Boolean hasFading;
    private Boolean isStretched;
    private String note;
}
