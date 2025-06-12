package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectorServiceImpl implements InspectorService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductSearchResultDto> searchProducts(Long inspectorId, ProductSearchRequestDto dto) {
        System.out.println((dto.isInspected()?"true":"false")+" "+dto.getKeyword());
        if (dto.isInspected()) {
            return productMapper.getInspectionCompletedProductsByInspectorAndKeyword(inspectorId, dto.getKeyword());
        } else {
            return productMapper.getPendingInspectionProductsByInspectorAndKeyword(inspectorId, dto.getKeyword());
        }
    }

}
