package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.PickupSearchRequest;
import com.earth.ureverse.admin.dto.request.ProductSearchRequest;
import com.earth.ureverse.admin.dto.response.*;
import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.common.response.PaginationResponse;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private final ProductStatusAsyncService productStatusAsyncService;

    @Override
    public PaginationResponse<FinishProductResponse> getFinishProducts(ProductSearchRequest request) {
        List<FinishProductResponse> items = productMapper.getFinishProducts(request);
        long total = productMapper.countFinishProducts(request);
        return new PaginationResponse<>(items, total, request.getPageNum(), request.getPageSize());
    }

    @Override
    public PaginationResponse<PickupProductResponse> getPickupProducts(PickupSearchRequest request) {
        List<PickupProductResponse> items = productMapper.getPickupProducts(request);
        long total = productMapper.countPickupProducts(request);
        return new PaginationResponse<>(items, total, request.getPageNum(), request.getPageSize());
    }

    @Override
    public ProductInspectionResultResponse getFinishProductDetail(Long productId) {
        productValidator.validateProductExists(productId);
        productValidator.validateProductFinish(productId);

        ProductDetailResponse product = productMapper.getProductDetail(productId);
        InspectionResultResponse aiResult = productMapper.getAiInspection(productId);
        InspectionResultResponse inspectorResult = productMapper.getHumanInspection(productId);
        String grade = productMapper.getProductGrade(productId);

        List<String> images = productMapper.getProductImages(productId);
        product.setImages(images);
        return new ProductInspectionResultResponse(product, aiResult, inspectorResult, grade);
    }

    @Override
    public PickupProductDetailResponse getPickupProductDetail(Long productId) {
        productValidator.validateProductExists(productId);
        productValidator.validateProductPickup(productId);

        ProductDetailResponse product = productMapper.getProductDetail(productId);
        InspectionResultResponse aiResult = productMapper.getAiInspection(productId);
        InspectionResultResponse inspectorResult = productMapper.getHumanInspection(productId);
        DeliveryResponse delivery = productMapper.getDelivery(productId);
        String grade = productMapper.getProductGrade(productId);

        List<String> images = productMapper.getProductImages(productId);
        product.setImages(images);
        return new PickupProductDetailResponse(product, aiResult, inspectorResult, delivery, grade);
    }

    @Override
    public void requestPickup(CustomUserDetails customUserDetails, Long productId) {
        Long adminId = customUserDetails.getUserId();

        productValidator.validateProductExists(productId);
        productValidator.validateProductSecondInspect(productId);

        productStatusAsyncService.updateStatus(productId, "DELIVERY_REQUEST", adminId); //1차 배송 요청으로 상태 변경

        productStatusAsyncService.updateStatusWithDelay(productId, adminId);
    }


    @Override
    public DashBoardSummaryResponse getDashBoardSummary(String date) {
        if(!isValidDateTimeFormat(date)){
            throw new IllegalArgumentException("date형식이 아닙니다.");
        }
        Integer pickupRequest = productMapper.getPickupRequest(); //현재 SECOND_INSPECT인_건수
        Long totalPaidPoint = productMapper.getTotalPaidPoint(date); //해당 날짜의 지급포인트 총합
        LocalDate[] range = getWeekRange(date);
        LocalDate startOfWeek = range[0];
        LocalDate endOfWeek = range[1];
        List<DashBoardBrandResponse> topBrands = productMapper.getTopBrandsOfWeek(startOfWeek.toString(), endOfWeek.toString());

        return new DashBoardSummaryResponse(pickupRequest, totalPaidPoint, topBrands);
    }
    private Boolean isValidDateTimeFormat(String date){
        if (date == null) return false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    private LocalDate[] getWeekRange(String dateStr){
        LocalDate date = LocalDate.parse(dateStr);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalDate startOfWeek = date.minusDays(dayOfWeek.getValue());
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return new LocalDate[]{startOfWeek, endOfWeek}; //월~일
    }

    @Override
    public DashBoardInspectionResultRatioResponse getInspectionResultRatio(String date, String method) {
        if(!isValidDateTimeFormat(date)){
            throw new IllegalArgumentException("date형식이 아닙니다.");
        }
        if (!("AI".equalsIgnoreCase(method) || "Human".equalsIgnoreCase(method))) {
            throw new IllegalArgumentException("검수 방식은 AI 또는 Human이어야 합니다.");
        }

        DashBoardInspectionResultRatioResponse response = productMapper.getInspectionResultRatio(date, method);
        response.setPassRatio(calcRatio(response.getPassCount(), response.getTotalCount()));
        response.setFailRatio(calcRatio(response.getFailCount(), response.getTotalCount()));

        return response;
    }

    @Override
    public DashBoardInspectionDefectRatioResponse getInspectionDefectRatio(String date, String method) {
        if(!isValidDateTimeFormat(date)){
            throw new IllegalArgumentException("date형식이 아닙니다.");
        }
        if (!("AI".equalsIgnoreCase(method) || "Human".equalsIgnoreCase(method))) {
            throw new IllegalArgumentException("검수 방식은 AI 또는 Human이어야 합니다.");
        }

        DashBoardInspectionDefectRatioResponse response = productMapper.getInspectionDefectRatio(date, method);
        int totalDefectCount = response.getTornCount() + response.getStainCount() + response.getFadingCount() +
                response.getStretchedCount() + response.getOtherCount();
        response.setTornRatio(calcRatio(response.getTornCount(), totalDefectCount));
        response.setStainRatio(calcRatio(response.getStainCount(), totalDefectCount));
        response.setFadingRatio(calcRatio(response.getFadingCount(), totalDefectCount));
        response.setStretchedRatio(calcRatio(response.getStretchedCount(), totalDefectCount));
        response.setOtherRatio(calcRatio(response.getOtherCount(), totalDefectCount));

        return response;
    }
    private double calcRatio(Integer count, Integer total) {
        if (count == null || total==null || total == 0) return 0.0;
        return Math.round((count * 100.0 / total) * 100) / 100.0;
    }
}
