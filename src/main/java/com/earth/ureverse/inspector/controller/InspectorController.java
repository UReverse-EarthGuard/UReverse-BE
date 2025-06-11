package com.earth.ureverse.inspector.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import com.earth.ureverse.inspector.service.InspectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/api/v1/inspectors")
@RestController
public class InspectorController {

    @Autowired
    private InspectorService inspectorService;

    // 상품 검수 필요 상품 목록 조회
    @GetMapping("/pending-inspection")
    public List<PendingInspectionProductDto> getPendingInspectionProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (!"INSPECTOR".equals(userDetails.getRole())) {
            throw new AccessDeniedException("검수자 권한이 필요합니다.");
        }
        return inspectorService.getPendingInspectionProductsByInspector(userDetails.getUserId());
    }
}
