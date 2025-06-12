package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.inspector.service.InspectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/inspectors")
@RestController
public class InspectorController {

    private final InspectorService inspectorService;

    @GetMapping("/products/search")
    public ResponseEntity<CommonResponseEntity<Object>> searchProductsByInspector(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            ProductSearchRequestDto requestDto
    ) {
        List<ProductSearchResultDto> result = inspectorService.searchProducts(
                userDetails.getUserId(), requestDto
        );
        return ResponseEntity.ok(CommonResponseEntity.success(result));
    }

    // 검수 필요 상품 상세 조회
    @GetMapping("/products/{productId}/pending")
    public ResponseEntity<CommonResponseEntity<Object>> getPendingProductDetail(
            @PathVariable Long productId
    ) {
        try {
            ProductInspectionDetailDto dto = inspectorService.getPendingProductDetail(productId);
            return ResponseEntity.ok(CommonResponseEntity.success(dto));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CommonResponseEntity.error(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

}
