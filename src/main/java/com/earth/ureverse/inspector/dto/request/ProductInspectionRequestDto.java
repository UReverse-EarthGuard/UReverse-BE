package com.earth.ureverse.inspector.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductInspectionRequestDto {
    @NotNull
    private Long productId;
    @NotBlank
    private String result;
    private String notes;
    @NotBlank
    private String isTorn; // Y/N
    @NotBlank
    private String hasStain;
    @NotBlank
    private String hasFading;
    @NotBlank
    private String isStretched;
    @NotBlank
    private String otherDefect;
    @NotBlank
    private String grade; // S, A, B, C, F
}

