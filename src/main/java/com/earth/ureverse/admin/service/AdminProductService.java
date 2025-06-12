package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.admin.dto.response.ProductInspectionResultResponse;

import java.util.List;

public interface AdminProductService {
    List<FinishProductResponse> getFinishProducts();

    List<PickupProductResponse> getPickupProducts();

    ProductInspectionResultResponse getFinishProductDetail(Long productId);
}
