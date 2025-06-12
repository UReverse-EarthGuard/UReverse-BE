package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;
import com.earth.ureverse.admin.dto.response.PickupProductResponse;
import com.earth.ureverse.admin.dto.response.ProductInspectionResultResponse;
import com.earth.ureverse.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductMapper productMapper;

    @Override
    public List<FinishProductResponse> getFinishProducts() {
        return productMapper.getFinishProducts();
    }

    @Override
    public List<PickupProductResponse> getPickupProducts() {
        return productMapper.getPickupProducts();
    }

    @Override
    public ProductInspectionResultResponse getFinishProductDetail(Long productId) {
        ProductInspectionResultResponse result = productMapper.getFinishProductDetail(productId);
        List<String> images = productMapper.getProductImages(productId);
        if (result != null && result.getProduct() != null) {
            result.getProduct().setImages(images);
        }
        return result;
    }
}
