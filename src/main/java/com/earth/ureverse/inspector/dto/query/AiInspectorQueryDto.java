package com.earth.ureverse.inspector.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiInspectorQueryDto {
    private Long productId;
    private String inspectMethod;
    private String notes;
    private String result;
    private String isTorn;
    private String hasStain;
    private String hasFading;
    private String isStretched;
    private String otherDefect;

    @Builder
    public AiInspectorQueryDto(Long productId, String inspectMethod, String notes, String result, String isTorn, String hasStain, String hasFading, String isStretched, String otherDefect) {
        this.productId = productId;
        this.inspectMethod = inspectMethod;
        this.notes = notes;
        this.result = result;
        this.isTorn = isTorn;
        this.hasStain = hasStain;
        this.hasFading = hasFading;
        this.isStretched = isStretched;
        this.otherDefect = otherDefect;
    }
}
