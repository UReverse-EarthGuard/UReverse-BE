package com.earth.ureverse.member.service;

import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.common.exception.AlreadyWithdrawnException;
import com.earth.ureverse.global.common.exception.IllegalParameterException;
import com.earth.ureverse.global.common.exception.PasswordMismatchException;
import com.earth.ureverse.global.enums.ProductStatus;
import com.earth.ureverse.global.mapper.DeliveryMapper;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.inspector.service.AiService;
import com.earth.ureverse.member.dto.request.ChangePasswordRequestDto;
import com.earth.ureverse.member.dto.request.ProductUploadRequestDto;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.dto.response.*;
import com.earth.ureverse.member.mapper.MemberMapper;
import com.earth.ureverse.member.mapper.PointMapper;
import com.earth.ureverse.member.mapper.ProductImageMapper;
import com.earth.ureverse.member.mapper.SalesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final AuthMapper authMapper;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final PointMapper pointMapper;
    private final SalesMapper salesMapper;
    private final ProductMapper productMapper;
    private final DeliveryMapper deliveryMapper;
    private final ProductImageMapper productImageMapper;
    private final AiService aiService;

    @Override
    public void withdraw(Long userId, WithdrawRequestDto withdrawRequestDto) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        if (!passwordEncoder.matches(withdrawRequestDto.getPassword(), authenticatedUser.getPassword())) {
            throw new PasswordMismatchException();
        }

        memberMapper.updateIsActive(userId, "inactive");
    }

    @Override
    public void updateMember(Long userId, UpdateMemberRequestDto updateMemberRequestDto) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        // 이메일 변경 시 비밀번호 확인 + 중복 체크
        if (updateMemberRequestDto.getEmail() != null && !updateMemberRequestDto.getEmail().equals(authenticatedUser.getEmail())) {
            if (updateMemberRequestDto.getPassword() == null ||
                    !passwordEncoder.matches(updateMemberRequestDto.getPassword(), authenticatedUser.getPassword())) {
                throw new PasswordMismatchException();
            }

            if (authMapper.existsByEmail(updateMemberRequestDto.getEmail())) {
                throw new IllegalParameterException("이미 존재하는 이메일입니다.");
            }
        }

        memberMapper.updateMember(userId, updateMemberRequestDto);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequestDto changePasswordRequestDto) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        if (!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), authenticatedUser.getPassword())) {
            throw new PasswordMismatchException();
        }

        String encodedPassword = passwordEncoder.encode(changePasswordRequestDto.getNewPassword());
        memberMapper.updatePassword(userId, encodedPassword);
    }

    @Override
    public PointHistoryListResponseDto getPointHistory(Long userId, int limit, String lastCreatedAt, Long lastProductId) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        validateCursorRequest(limit, lastCreatedAt);

        List<PointHistoryResponseDto> pointHistory = pointMapper.getPointHistory(userId, limit, lastCreatedAt, lastProductId);
        String newLastCreatedAt = null;
        Long newLastProductId = null;

        if (!pointHistory.isEmpty()) {
            PointHistoryResponseDto lastItem = pointHistory.get(pointHistory.size() - 1);
            newLastCreatedAt = lastItem.getCreatedAt();
            newLastProductId = lastItem.getProductId();
        }

        int totalPoint = pointMapper.getTotalPoint(userId);

        return new PointHistoryListResponseDto(totalPoint, pointHistory, newLastCreatedAt, newLastProductId);
    }

    @Override
    public SalesHistoryListResponseDto getSalesHistory(Long userId, int limit, String lastCreatedAt, Long lastProductId) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        validateCursorRequest(limit, lastCreatedAt);

        List<SalesHistoryResponseDto> salesHistory = salesMapper.getSalesHistory(userId, limit, lastCreatedAt, lastProductId);
        String newLastCreatedAt = null;
        Long newLastProductId = null;

        if (!salesHistory.isEmpty()) {
            SalesHistoryResponseDto lastItem = salesHistory.get(salesHistory.size() - 1);
            newLastCreatedAt = lastItem.getCreatedAt();
            newLastProductId = lastItem.getProductId();
        }

        return new SalesHistoryListResponseDto(salesHistory, newLastCreatedAt, newLastProductId);
    }

    private void validateCursorRequest(int limit, String lastCreatedAt) {
        if (limit <= 0) {
            throw new IllegalParameterException("limit은 1 이상의 값이어야 합니다.");
        }

        if (lastCreatedAt != null && !isValidDateTimeFormat(lastCreatedAt)) {
            throw new IllegalParameterException("lastCreatedAt의 형식이 잘못되었습니다. (예: 2025-06-13 14:00:00)");
        }
    }

    @Override
    public MemberInfoResponseDto getMyInfo(Long userId) {
        MemberInfoResponseDto memberInfoResponseDto = memberMapper.findMyInfoByUserId(userId);
        List<Map<String, Object>> rawList = memberMapper.countProductStatus(userId);
        Map<String, Integer> statusMap = toKoreanStatusMap(rawList);
        memberInfoResponseDto.setProductStatus(statusMap);

        return memberInfoResponseDto;
    }

    private Map<String, Integer> toKoreanStatusMap(List<Map<String, Object>> rawList) {
        Map<String, Integer> result = new LinkedHashMap<>();

        for (ProductStatus status : ProductStatus.values()) {
            result.put(status.getLabel(), 0);
        }

        for (Map<String, Object> row : rawList) {
            String statusCode = (String) row.get("STATUS");
            String label = ProductStatus.getLabelByCode(statusCode);
            BigDecimal count = (BigDecimal) row.get("VALUE");
            result.put(label, count.intValue());
        }

        return result;
    }

    // 유효한 날짜 형식인지 확인 (yyyy-MM-dd HH:mm:ss)
    private boolean isValidDateTimeFormat(String lastCreatedAt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime.parse(lastCreatedAt, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void registerProduct(ProductUploadRequestDto dto, Long userId){

        // 시퀀스로 insert 될 productId 미리 조회
        Long productId = productMapper.getNextProductId();

        // 상품 데이터 등록
        productMapper.insertProduct(productId, userId, dto);

        // 배송정보 데이터 등록
        deliveryMapper.insertDelivery(productId, userId, dto);

        // productId 를 FK 로 가지는 image 객체에 url 저장
        for(String url : dto.getProductsImageUrl()) {
            productImageMapper.insertProductImage(productId, url, userId);
        }

        // AI 비동기 호출
        aiService.aiInspect(dto.getProductsImageUrl(), dto.getCategory(), dto.getBrandName(), productId, dto.getName(), userId);
    }

}
