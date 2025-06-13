package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.*;
import com.earth.ureverse.global.common.response.PaginationResponse;

import java.util.List;

public interface AdminProductService {
    PaginationResponse<FinishProductResponse> getFinishProducts(ProductSearchRequest request);

    PaginationResponse<PickupProductResponse> getPickupProducts(PickupSearchRequest request);

    ProductInspectionResultResponse getFinishProductDetail(Long productId);

    PickupProductDetailResponse getPickupProductDetail(Long productId);

    DashBoardSummaryResponse getDashBoardSummary(String date);
}
