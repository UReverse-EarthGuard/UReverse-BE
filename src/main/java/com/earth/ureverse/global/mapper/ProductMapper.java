package com.earth.ureverse.global.mapper;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<FinishProductResponse> getFinishProducts(ProductSearchRequest request);

    long countFinishProducts(ProductSearchRequest request);

    List<ProductSearchResultDto> getPendingInspectionProductsByInspectorAndKeyword(
            @Param("inspectorId") Long inspectorId, @Param("keyword") String keyword, @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    List<ProductSearchResultDto> getInspectionCompletedProductsByInspectorAndKeyword(
            @Param("inspectorId") Long inspectorId, @Param("keyword") String keyword, @Param("offset") int offset,
            @Param("pageSize") int pageSize);

    List<PickupProductResponse> getPickupProducts(PickupSearchRequest request);

    long countPickupProducts(PickupSearchRequest request);
}
