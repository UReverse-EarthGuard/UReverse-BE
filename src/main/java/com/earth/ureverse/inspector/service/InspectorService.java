package com.earth.ureverse.inspector.service;

import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;

import java.util.List;

public interface InspectorService {

    List<ProductSearchResultDto> searchProducts(Long userId, ProductSearchRequestDto requestDto);
}
