package com.earth.ureverse.global.notification.controller;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.global.notification.repository.SseEmitterRepository;
import com.earth.ureverse.global.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final SseEmitterRepository emitterRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId, @RequestParam("token") String token) {
        // 1. 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AccessDeniedException("유효하지 않은 토큰입니다.");
        }

        // 2. 토큰에서 사용자 ID 추출
        Long tokenUserId = Long.valueOf(jwtTokenProvider.getUserIdFromToken(token));

        // 3. URL로 요청한 userId와 토큰의 userId 비교
        if (!tokenUserId.equals(userId)) {
            throw new AccessDeniedException("사용자 정보 불일치");
        }

        // 4. Emitter 연결
        SseEmitter emitter = new SseEmitter(60 * 1000L);
        emitterRepository.save(userId, emitter);
        try {
            emitter.send(SseEmitter.event().name("connect").data("SSE 연결 성공"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @PostMapping("/test-send")
    public ResponseEntity<String> sendTestNotification(@RequestParam Long userId) {
        NotificationDto dto = new NotificationDto(
                null,
                userId,
                "테스트 알림",
                "이것은 테스트 메시지입니다",
                "TEST",
                "N",
                LocalDateTime.now()
        );

        notificationService.sendNotification(dto);

        return ResponseEntity.ok("알림 전송 완료");
    }

    @GetMapping("/unread-count")
    public CommonResponseEntity<Integer> getUnreadCount(@AuthenticationPrincipal CustomUserDetails user) {
        int count = notificationService.countUnread(user.getUserId());
        System.out.println("카운트수 : "+count);
        return CommonResponseEntity.success(count);
    }

}
