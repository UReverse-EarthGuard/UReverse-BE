package com.earth.ureverse.admin.controller;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.*;
import com.earth.ureverse.admin.service.AdminProductService;
import com.earth.ureverse.admin.service.AdminUserService;
import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.common.response.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin", description = "관리자 서비스 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminProductService adminProductService;
    private final AdminUserService adminUserService;

    @GetMapping("/products/finish")
    public CommonResponseEntity<PaginationResponse<FinishProductResponse>> getFinishProducts(
            @RequestBody ProductSearchRequest request
    ) {
        return CommonResponseEntity.success(adminProductService.getFinishProducts(request));
    }

    @GetMapping("/products/pickup")
    public CommonResponseEntity<PaginationResponse<PickupProductResponse>> getPickupProducts(
            @RequestBody PickupSearchRequest request
    ) {
        return CommonResponseEntity.success(adminProductService.getPickupProducts(request));
    }

    @GetMapping("/products/finish/{productId}")
    public CommonResponseEntity<ProductInspectionResultResponse> getFinishProductDetail(@PathVariable Long productId) {
        return CommonResponseEntity.success(adminProductService.getFinishProductDetail(productId));
    }

    @GetMapping("/products/pickup/{productId}")
    public CommonResponseEntity<PickupProductDetailResponse> getPickupProductDetail(@PathVariable Long productId) {
        return CommonResponseEntity.success(adminProductService.getPickupProductDetail(productId));
    }

    @GetMapping("/users")
    public CommonResponseEntity<PaginationResponse<ActiveMemberResponse>> getActiveUsers(
            @RequestBody ActiveMemberSearchRequest request
    ) {
        return CommonResponseEntity.success(adminUserService.getActiveUsers(request));
    }

    @GetMapping("/dash-boards/summary/{date}")
    public CommonResponseEntity<DashBoardSummaryResponse> getDashBoardSummary(
            @PathVariable String date
    ) {
        return CommonResponseEntity.success(adminProductService.getDashBoardSummary(date));
    }

    @PostMapping("/products/{productId}/pickup")
    public CommonResponseEntity<String> requestPickup(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long productId
    ) {
        adminProductService.requestPickup(customUserDetails, productId);
        return CommonResponseEntity.success("수거 요청이 등록되었습니다.");
    }

    @GetMapping("/dash-boards/inspection-result/{date}/{method}")
    public CommonResponseEntity<DashBoardInspectionResultRatioResponse> getInspectionResultRatio(
            @PathVariable String date,
            @PathVariable String method
    ){
        return CommonResponseEntity.success(adminProductService.getInspectionResultRatio(date, method));
    }



    @GetMapping("/dash-boards/inspection-defect/{date}/{method}")
    public CommonResponseEntity<DashBoardInspectionDefectRatioResponse> getInspectionDefectRatio(
            @PathVariable String date,
            @PathVariable String method
    ){
        return CommonResponseEntity.success(adminProductService.getInspectionDefectRatio(date, method));
    }

    @GetMapping("/dash-boards/finish-stats/{range}")
    public CommonResponseEntity<List<DashBoardFinishProductResponse>> getFinishStats(@PathVariable String range){
        return CommonResponseEntity.success(adminProductService.getFinishStats(range));
    }
}
