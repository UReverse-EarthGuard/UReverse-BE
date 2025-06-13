package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.admin.dto.response.ProductInspectionResultResponse;
import com.earth.ureverse.global.common.exception.NotFoundException;
import com.earth.ureverse.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductMapper productMapper;

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
        ProductInspectionResultResponse result = productMapper.getFinishProductDetail(productId);
        if(result == null || result.getProduct() ==null){
            throw new NotFoundException("해당 productId의 상품을 찾을 수 없습니다.");
        }
        List<String> images = productMapper.getProductImages(productId);
        result.getProduct().setImages(images);
        return result;
    }
}
