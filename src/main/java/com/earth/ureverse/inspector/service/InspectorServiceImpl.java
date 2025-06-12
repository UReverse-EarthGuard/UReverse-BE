package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InspectorServiceImpl implements InspectorService{

    @Autowired
    private ProductMapper productMapper;

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

}
