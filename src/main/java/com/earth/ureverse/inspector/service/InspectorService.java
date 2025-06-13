package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.inspector.dto.request.ProductInspectionRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;

import java.util.List;

public interface InspectorService {

    ProductInspectionDetailDto getPendingProductDetail(Long productId);

    PaginationResponse<ProductSearchResultDto> searchProducts(Long userId, ProductSearchRequestDto requestDto);

    void inspectProduct(Long userId, ProductInspectionRequestDto dto);
    ProductInspectedDetailDto getInspectedProductDetail(Long productId);
}
