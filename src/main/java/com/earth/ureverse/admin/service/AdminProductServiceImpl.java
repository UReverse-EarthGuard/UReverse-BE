package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.global.common.response.PaginationResponse;
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
        return new PaginationResponse<>(items, total);
    }

    @Override
    public PaginationResponse<PickupProductResponse> getPickupProducts(PickupSearchRequest request) {
        List<PickupProductResponse> items = productMapper.getPickupProducts(request);
        long total = productMapper.countPickupProducts(request);
        return new PaginationResponse<>(items, total);
    }
}
