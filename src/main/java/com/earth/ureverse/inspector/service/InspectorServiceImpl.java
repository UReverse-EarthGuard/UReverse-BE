package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.common.exception.IllegalParameterException;
import com.earth.ureverse.global.common.exception.NotFoundException;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.ProductValidator;
import com.earth.ureverse.inspector.dto.request.ProductInspectionRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            throw new NotFoundException("해당 상품이 존재하지 않습니다: productId = " + productId);
        }
        return detail;
    }

    @Override
    @Transactional
    public void inspectProduct(Long inspectorId, ProductInspectionRequestDto dto) {
        // 검증
        productValidator.validateProductExists(dto.getProductId());
        productValidator.validateProductStatusIsFirstInspect(dto.getProductId());

        int ratio = switch (dto.getGrade()) {
            case "S" -> 100;
            case "A" -> 70;
            case "B" -> 50;
            case "C" -> 30;
            case "F" -> 0;
            default -> throw new IllegalParameterException("Invalid grade");
        };

        productMapper.insertInspection(inspectorId, dto);

        String status = "FAIL".equals(dto.getResult()) ? "REJECT" : "SECOND_INSPECT";

        Long paidPoint = 0L;
        if (ratio > 0) {
            Long expectedPoint = productMapper.getExpectedPoint(dto.getProductId());
            paidPoint = expectedPoint * ratio / 100;
        }

        productMapper.updateProductAfterInspection(dto.getProductId(), dto.getGrade(), paidPoint, status, inspectorId);
    }

}
