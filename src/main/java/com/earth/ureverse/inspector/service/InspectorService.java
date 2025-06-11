package com.earth.ureverse.inspector.service;

import com.earth.ureverse.inspector.dto.response.InspectionCompletedProductDto;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;

import java.util.List;

public interface InspectorService {
    List<PendingInspectionProductDto> getPendingInspectionProductsByInspector(Long inspectorId);

    List<InspectionCompletedProductDto> getInspectionCompletedProductsByInspector(Long inspectorId);
}
