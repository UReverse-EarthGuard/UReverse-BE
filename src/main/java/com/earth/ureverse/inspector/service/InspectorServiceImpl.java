package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.common.exception.NotFoundException;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.ProductValidator;
import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class InspectorServiceImpl implements InspectorService{

    private final ProductMapper productMapper;
    private final ProductValidator productValidator;

    @Override
    public List<ProductSearchResultDto> searchProducts(Long inspectorId, ProductSearchRequestDto dto) {

        int pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 6;
        int offset = (pageNum - 1) * pageSize;

        if (dto.isInspected()) {
            return productMapper
                    .getInspectionCompletedProductsByInspectorAndKeyword(inspectorId, dto.getKeyword(), offset, pageSize);
        } else {
            return productMapper
                    .getPendingInspectionProductsByInspectorAndKeyword(inspectorId, dto.getKeyword(), offset, pageSize);
        }
    }

    @Override
    public ProductInspectionDetailDto getPendingProductDetail(Long productId) {
        ProductInspectionDetailDto detail = productMapper.getPendingProductDetail(productId);
        if (detail == null) {
            throw new NoSuchElementException("해당 상품이 존재하지 않습니다: productId = " + productId);
        }
        return detail;
    }

    @Override
    public ProductInspectedDetailDto getInspectedProductDetail(Long productId) {
        // 상품 확인
        productValidator.validateProductExists(productId);
        productValidator.validateProductSecondInspected(productId);

        ProductInspectedDetailDto dto = productMapper.getInspectedProductDetail(productId);
        if (dto == null) {
            throw new NotFoundException("검수 완료된 상품을 찾을 수 없습니다: productId = " + productId);
        }
        return dto;
    }
}
