package com.earth.ureverse.global.mapper;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.inspector.dto.response.InspectionCompletedProductDto;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<PendingInspectionProductDto> getPendingInspectionProductsByInspector(@Param("inspectorId") Long inspectorId);

    List<FinishProductResponse> getFinishProducts();

    List<InspectionCompletedProductDto> getInspectionCompletedProductsByInspector(@Param("inspectorId") Long inspectorId);
}
