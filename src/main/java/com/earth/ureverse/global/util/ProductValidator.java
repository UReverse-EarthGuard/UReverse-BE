package com.earth.ureverse.global.util;

import com.earth.ureverse.global.common.exception.BadRequestException;
import com.earth.ureverse.global.common.exception.NotFoundException;
import com.earth.ureverse.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductMapper productMapper;

    public void validateProductExists(Long productId) {
        boolean exists = productMapper.existsByProductId(productId);
        if (!exists) {
            throw new NotFoundException("해당 상품이 존재하지 않습니다: productId = " + productId);
        }
    }

    public void validateProductSecondInspected(Long productId) {
        String status = productMapper.getProductStatus(productId);
        if (status == null) {
            throw new BadRequestException("상품 상태를 조회할 수 없습니다: productId = " + productId);
        }

        Set<String> allowedStatuses = Set.of("SECOND_INSPECT", "DELIVERY_REQUEST", "DELIVERING", "FINISH");
        if (!allowedStatuses.contains(status)) {
            throw new BadRequestException("검수 완료된 상품 상태가 아닙니다. 현재 상태: " + status);
        }
    }

    public void validateProductFinish(Long productId) {
        String status = productMapper.getProductStatus(productId);
        if (status == null) {
            throw new BadRequestException("상품 상태를 조회할 수 없습니다: productId = " + productId);
        }

        if (!status.equals("FINISH")) {
            throw new BadRequestException("상품 상태가 FINISH가 아닙니다. 현재 상태: " + status);
        }
    }

    public void validateProductPickup(Long productId) {
        String status = productMapper.getProductStatus(productId);
        if (status == null) {
            throw new BadRequestException("상품 상태를 조회할 수 없습니다: productId = " + productId);
        }
        Set<String> allowedStatuses = Set.of("REGISTER", "FIRST_INSPECT", "SECOND_INSPECT", "DELIVERY_REQUEST", "DELIVERING", "FINISH");
        if (!allowedStatuses.contains(status)) {
            throw new BadRequestException("수거관리 대상이 아닙니다. 현재 상태: " + status);
        }
    }
}
