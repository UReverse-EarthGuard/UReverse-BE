package com.earth.ureverse.inspector.service;

import com.earth.ureverse.inspector.dto.response.InspectionCompletedProductDto;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;

import java.util.List;

public interface InspectorService {
    List<PendingInspectionProductDto> getPendingInspectionProductsByInspector(Long inspectorId);

    List<InspectionCompletedProductDto> getInspectionCompletedProductsByInspector(Long inspectorId);

    ProductInspectionDetailDto getPendingProductDetail(Long productId);
}
