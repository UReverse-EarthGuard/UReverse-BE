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
    private Boolean tear;
    private Boolean stain;
    private Boolean fading;
    private Boolean stretching;
    private String comment;
}
