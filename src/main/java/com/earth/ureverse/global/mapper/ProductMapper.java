package com.earth.ureverse.global.mapper;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<FinishProductResponse> getFinishProducts();

    List<ProductSearchResultDto> getPendingInspectionProductsByInspectorAndKeyword(@Param("inspectorId") Long inspectorId, @Param("keyword") String keyword);

    List<ProductSearchResultDto> getInspectionCompletedProductsByInspectorAndKeyword(@Param("inspectorId") Long inspectorId, @Param("keyword") String keyword);

    List<PickupProductResponse> getPickupProducts();
}
