package com.earth.ureverse.inspector.service;

import com.earth.ureverse.admin.dto.response.ProductDetailResponse;
import com.earth.ureverse.global.common.exception.IllegalParameterException;
import com.earth.ureverse.global.common.exception.NotFoundException;
import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.ProductValidator;
import com.earth.ureverse.inspector.dto.request.ProductInspectionRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectedDetailDto;
import com.earth.ureverse.inspector.dto.response.ProductInspectionDetailDto;
import com.earth.ureverse.inspector.dto.request.ProductSearchRequestDto;
import com.earth.ureverse.inspector.dto.response.ProductSearchResultDto;
import com.earth.ureverse.inspector.mapper.InspectorMapper;
import com.earth.ureverse.member.dto.query.NotificationQueryDto;
import com.earth.ureverse.member.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InspectorServiceImpl implements InspectorService{

    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final InspectorMapper inspectorMapper;
    private final NotificationMapper notificationMapper;

    @Override
    public PaginationResponse<ProductSearchResultDto> searchProducts(Long inspectorId, ProductSearchRequestDto dto) {
        int pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        int pageSize = dto.getPageSize() != null ? dto.getPageSize() : 6;
        int offset = (pageNum - 1) * pageSize;
        String keyword = dto.getKeyword();

        boolean isInspected = dto.isInspected();

        List<ProductSearchResultDto> items = isInspected
                ? productMapper.getInspectionCompletedProductsByInspectorAndKeyword(inspectorId, keyword, offset, pageSize)
                : productMapper.getPendingInspectionProductsByInspectorAndKeyword(inspectorId, keyword, offset, pageSize);

        int total = isInspected
                ? productMapper.countInspectionCompletedProductsByInspectorAndKeyword(inspectorId, keyword)
                : productMapper.countPendingInspectionProductsByInspectorAndKeyword(inspectorId, keyword);

        return new PaginationResponse<>(items, total, pageNum, pageSize);
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

        // Notification 테이블 저장
        // 결과 메시지 분기 처리
        String result = dto.getResult(); // "PASS" or "FAIL"
        String message;

        ProductDetailResponse productDetail = productMapper.getProductDetail(dto.getProductId());

        if ("PASS".equalsIgnoreCase(result)) {
            message = String.format(
                    "등록하신 [%s] 브랜드의 [%s] 상품이 최종 검수를 통과했습니다. 곧 제품 수거가 진행될 예정입니다.",
                    productDetail.getBrand(), (productDetail.getCategoryMain() + " / " + productDetail.getCategorySub())
            );
        } else {
            message = String.format(
                    "등록하신 [%s] 브랜드의 [%s] 상품이 최종 검수에 실패했습니다. 다시 한 번 확인 후 재등록 부탁드립니다.",
                    productDetail.getBrand(), (productDetail.getCategoryMain() + " / " + productDetail.getCategorySub())
            );
        }

        // notification DB 저장
        NotificationQueryDto notificationQueryDto = NotificationQueryDto.builder()
                .notificationType("AI_RESULT")
                .userId(productDetail.getUserId())
                .title("AI 검수결과 안내")
                .message(message)
                .isRead("N")
                .build();

        notificationMapper.insert(notificationQueryDto);
    }

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
