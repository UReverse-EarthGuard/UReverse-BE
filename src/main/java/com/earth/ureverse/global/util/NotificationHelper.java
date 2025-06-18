package com.earth.ureverse.global.util;

import com.earth.ureverse.admin.dto.query.ProductNotificationInfoDto;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.global.notification.service.NotificationService;
import com.earth.ureverse.member.dto.query.NotificationQueryDto;
import com.earth.ureverse.member.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationHelper {

    private final ProductMapper productMapper;
    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    /**
     *
     * @param productId 상품 PK
     * @param status 상품 테이블에 저장될 상태, 알림 메세지, 내용 결정용
     * @param notificationType 알림 타입
     * @param updatedBy 업데이트 한 사용자 PK
     */
    public void updateNotification(Long productId, String status, String notificationType, Long updatedBy) {
        log.info("update notification :: {}, {}, {}, {}", productId, status, notificationType, updatedBy);
        // productId 로 userId 조회
        ProductNotificationInfoDto productNotificationInfoDto = productMapper.getProductNotificationInfo(productId);
        Long userId = productNotificationInfoDto.getUserId();

        // 저장 현재시작
        LocalDateTime now = LocalDateTime.now();

        // notification pk 저장
        Long notificationId = notificationMapper.getNextNotificationId();

        String title = "";
        String message = "";
        String brand = productNotificationInfoDto.getBrand();
        String category = productNotificationInfoDto.getCategory();

        switch (status) {
            case "REJECT" -> {
                title = "AI 검수결과 안내";
                message = String.format(
                        "등록하신 [%s] 브랜드의 [%s] 상품이 이미지 검수에 실패했습니다. 다시 한 번 확인 후 재등록 부탁드립니다.",
                        brand, category
                );
            }
            case "FIRST_INSPECT" -> {
                title = "AI 검수결과 안내";
                message = String.format(
                        "등록하신 [%s] 브랜드의 [%s] 상품이 AI 검수를 통과했습니다. 곧 최종 검수가 진행될 예정입니다.",
                        brand, category
                );
            }
            case "SECOND_INSPECT" -> {
                title = "최종 검수결과 안내";
                message = String.format(
                        "등록하신 [%s] 브랜드의 [%s] 상품이 최종 검수를 통과했습니다. 곧 제품 수거가 진행될 예정입니다.",
                        brand, category
                );
            }
            case "DELIVERY_REQUEST" -> {
                title = "상품 수거 시작 안내";
                message = String.format(
                        "등록하신 [%s] 브랜드의 [%s] 상품 수거가 시작되었습니다. 상품을 준비해 주세요."
                        , brand, category
                );
            }
            case "DELIVERING" -> {
                title = "";
                message = "";
            }
            case "FINISH" -> {
                title = "알림";
                message = String.format(
                        "등록하신 [%s] 브랜드의 [%s] 상품이 수거 완료 되었습니다. 포인트 지급 내역을 확인해주세요."
                        , brand, category
                );
            }
        }

        if(!title.isEmpty() && !message.isEmpty()) {
            // notification DB 저장
            NotificationQueryDto notificationQueryDto = NotificationQueryDto.builder()
                    .notificationId(notificationId)
                    .notificationType(notificationType)
                    .userId(userId)
                    .title(title)
                    .message(message)
                    .isRead("N")
                    .timestamp(now)
                    .createUserId(updatedBy)
                    .build();
            log.info("DB 저장 통과");
            // SSE 알림 추가
            notificationService.sendNotification(new NotificationDto(
                    notificationId,
                    userId,
                    title,
                    message,
                    notificationType,
                    "N",
                    now
            ));
            log.info("알림 전송 통과");
            notificationMapper.insert(notificationQueryDto);
        }
    }
}
