package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.inspector.dto.response.InspectionCompletedProductDto;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.service.InspectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RequestMapping("/api/v1/inspectors")
@RestController
public class InspectorController {

    private final InspectorService inspectorService;

    // 상품 검수 필요 상품 목록 조회
    @GetMapping("/pending-inspection")
    public ResponseEntity<CommonResponseEntity<Object>> getPendingInspectionProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PendingInspectionProductDto> result =
                inspectorService.getPendingInspectionProductsByInspector(userDetails.getUserId());
        return ResponseEntity.ok(CommonResponseEntity.success(result));
    }

    // 검수 완료된 상품 목록 조회
    @GetMapping("/completed-products")
    public ResponseEntity<CommonResponseEntity<Object>> getInspectionCompletedProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<InspectionCompletedProductDto> result =
                inspectorService.getInspectionCompletedProductsByInspector(userDetails.getUserId());
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
