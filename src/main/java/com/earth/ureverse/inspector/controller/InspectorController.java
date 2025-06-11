package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import com.earth.ureverse.inspector.service.InspectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/inspectors")
@RestController
public class InspectorController {

    private final InspectorService inspectorService;

    // 상품 검수 필요 상품 목록 조회
    @GetMapping("/pending-inspection")
    public ResponseEntity<CommonResponseEntity<Object>> getPendingInspectionProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!"INSPECTOR".equals(userDetails.getRole())) {
            throw new AccessDeniedException("검수자 권한이 필요합니다.");
        }
        List<PendingInspectionProductDto> result =
                inspectorService.getPendingInspectionProductsByInspector(userDetails.getRole());
        return ResponseEntity.ok(CommonResponseEntity.success(result));
    }
}
