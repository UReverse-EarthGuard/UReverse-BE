package com.earth.ureverse.inspector.service;

import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;

import java.util.List;

public interface InspectorService {

    ProductInspectionDetailDto getPendingProductDetail(Long productId);

    List<ProductSearchResultDto> searchProducts(Long userId, ProductSearchRequestDto requestDto);

    ProductInspectedDetailDto getInspectedProductDetail(Long productId);
}
