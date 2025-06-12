package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.global.common.response.PaginationResponse;

import java.util.List;

public interface AdminProductService {
    PaginationResponse<FinishProductResponse> getFinishProducts(ProductSearchRequest request);

    PaginationResponse<PickupProductResponse> getPickupProducts(PickupSearchRequest request);
}
