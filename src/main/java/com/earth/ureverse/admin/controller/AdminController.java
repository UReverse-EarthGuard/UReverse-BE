package com.earth.ureverse.admin.controller;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.admin.dto.response.ProductInspectionResultResponse;
import com.earth.ureverse.admin.service.AdminProductService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminProductService adminProductService;

    @GetMapping("/products/finish")
    public CommonResponseEntity<List<FinishProductResponse>> getFinishProducts() {
        return CommonResponseEntity.success(adminProductService.getFinishProducts());
    }

    @GetMapping("/products/pickup")
    public CommonResponseEntity<List<PickupProductResponse>> getPickupProducts() {
        return CommonResponseEntity.success(adminProductService.getPickupProducts());
    }

    @GetMapping("/products/finish")
    public CommonResponseEntity<ProductInspectionResultResponse> getFinishProductDetail(@RequestParam("productId") Long productId){
        return CommonResponseEntity.success(adminProductService.getFinishProductDetail(productId));
    }
}
