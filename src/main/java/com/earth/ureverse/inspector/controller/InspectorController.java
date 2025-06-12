package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.inspector.service.InspectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
