package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.inspector.dto.response.InspectionCompletedProductDto;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InspectorServiceImpl implements InspectorService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<PendingInspectionProductDto> getPendingInspectionProductsByInspector(Long inspectorId) {
        return productMapper.getPendingInspectionProductsByInspector(inspectorId);
    }

    @Override
    public List<InspectionCompletedProductDto> getInspectionCompletedProductsByInspector(Long inspectorId) {
        return productMapper.getInspectionCompletedProductsByInspector(inspectorId);
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
