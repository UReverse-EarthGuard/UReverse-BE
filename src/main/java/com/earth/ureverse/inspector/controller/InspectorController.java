package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.inspector.dto.request.ProductInspectionRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.inspector.service.InspectorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Inspector", description = "검수자 서비스 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/inspectors")
@RestController
public class InspectorController {

    private final InspectorService inspectorService;

    // 검수 상품 검색
    @GetMapping("/products/search")
    public CommonResponseEntity<Object> searchProductsByInspector(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            ProductSearchRequestDto requestDto
    ) {
        PaginationResponse<ProductSearchResultDto> result = inspectorService.searchProducts(
                userDetails.getUserId(), requestDto
        );
        return CommonResponseEntity.success(result);
    }

    // 검수 필요 상품 상세 조회
    @GetMapping("/products/{productId}/pending")
    public CommonResponseEntity<Object> getPendingProductDetail(
            @PathVariable Long productId
    ) {
        ProductInspectionDetailDto dto = inspectorService.getPendingProductDetail(productId);
        return CommonResponseEntity.success(dto);
    }

    // 상품 검수 통과/탈락
    @PostMapping("/products/inspection")
    public CommonResponseEntity<Object> inspectProduct(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ProductInspectionRequestDto dto) {
        inspectorService.inspectProduct(userDetails.getUserId(), dto);
        return CommonResponseEntity.success("검사 완료");
    }

    // 검수 완료된 상품 상세 조회
    @GetMapping("/products/{productId}/inspected")
    public CommonResponseEntity<Object> getInspectedProductDetail(
            @PathVariable Long productId) {
        ProductInspectedDetailDto detail = inspectorService.getInspectedProductDetail(productId);
        return CommonResponseEntity.success(detail);
    }

}
