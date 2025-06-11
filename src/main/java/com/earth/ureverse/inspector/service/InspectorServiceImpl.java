package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.inspector.dto.response.PendingInspectionProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectorServiceImpl implements InspectorService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<PendingInspectionProductDto> getPendingInspectionProductsByInspector(Long inspectorId) {
        return productMapper.getPendingInspectionProductsByInspector(inspectorId);
    }
}
