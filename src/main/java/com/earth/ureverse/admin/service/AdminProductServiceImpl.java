package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.*;
import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    @Override
    public PaginationResponse<FinishProductResponse> getFinishProducts(ProductSearchRequest request) {
        List<FinishProductResponse> items = productMapper.getFinishProducts(request);
        long total = productMapper.countFinishProducts(request);
        return new PaginationResponse<>(items, total, request.getPageNum(), request.getPageSize());
    }

    @Override
    public PaginationResponse<PickupProductResponse> getPickupProducts(PickupSearchRequest request) {
        List<PickupProductResponse> items = productMapper.getPickupProducts(request);
        long total = productMapper.countPickupProducts(request);
        return new PaginationResponse<>(items, total, request.getPageNum(), request.getPageSize());
    }

    @Override
    public ProductInspectionResultResponse getFinishProductDetail(Long productId) {
        productValidator.validateProductExists(productId);
        productValidator.validateProductFinish(productId);

        ProductDetailResponse product = productMapper.getProductDetail(productId);
        InspectionResultResponse aiResult = productMapper.getAiInspection(productId);
        InspectionResultResponse inspectorResult = productMapper.getHumanInspection(productId);
        String grade = productMapper.getProductGrade(productId);

        List<String> images = productMapper.getProductImages(productId);
        product.setImages(images);
        return new ProductInspectionResultResponse(product, aiResult, inspectorResult, grade);
    }

    @Override
    public PickupProductDetailResponse getPickupProductDetail(Long productId) {
        productValidator.validateProductExists(productId);
        productValidator.validateProductPickup(productId);

        ProductDetailResponse product = productMapper.getProductDetail(productId);
        InspectionResultResponse aiResult = productMapper.getAiInspection(productId);
        InspectionResultResponse inspectorResult = productMapper.getHumanInspection(productId);
        DeliveryResponse delivery = productMapper.getDelivery(productId);
        String grade = productMapper.getProductGrade(productId);

        List<String> images = productMapper.getProductImages(productId);
        product.setImages(images);
        return new PickupProductDetailResponse(product, aiResult, inspectorResult, delivery, grade);
    }
}
