package com.earth.ureverse.admin.controller;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductDetailResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.admin.dto.response.ProductInspectionResultResponse;
import com.earth.ureverse.admin.service.AdminProductService;
import com.earth.ureverse.admin.service.AdminUserService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.common.response.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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
}
