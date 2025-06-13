package com.earth.ureverse.global.mapper;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.*;
import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
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

    List<String> getProductImages(Long productId);


    ProductDetailResponse getProductDetail(@Param("productId") Long productId);


    InspectionResultResponse getAiInspection(@Param("productId") Long productId);


    InspectionResultResponse getHumanInspection(@Param("productId") Long productId);


    String getProductGrade(@Param("productId") Long productId);

    ProductInspectionDetailDto getPendingProductDetail(@Param("productId") Long productId);

    ProductInspectedDetailDto getInspectedProductDetail(@Param("productId") Long productId);

    boolean existsByProductId(@Param("productId") Long productId);

    String getProductStatus(@Param("productId") Long productId);

    DeliveryResponse getDelivery(Long productId);
}
