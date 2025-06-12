package com.earth.ureverse.inspector.dto.response;

import lombok.Data;


@Data
public class InspectionResultDto {
    private String result;
    private String notes;
    private String inspectMethod;
    private String isTorn;
    private String hasStain;
    private String hasFading;
    private String isStretched;
    private String otherDefect;
    private String createdAt;
}
