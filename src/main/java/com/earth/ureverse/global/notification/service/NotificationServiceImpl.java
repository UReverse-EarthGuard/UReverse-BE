package com.earth.ureverse.global.notification.service;

import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.global.notification.repository.SseEmitterRepository;
import com.earth.ureverse.member.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SseEmitterRepository emitterRepository;
    private final NotificationMapper notificationMapper;

    // 알림 생성 시 호출
    @Override
    public void sendNotification(NotificationDto notification) {
        emitterRepository.get(notification.getUserId()).ifPresent(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
    }

    @Override
    public int countUnread(Long userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public void sendReadUpdate(Long userId, List<Long> notificationIdList) {
        emitterRepository.send(userId, SseEmitter.event()
                .name("read-update")
                .data(notificationIdList));
    }
}
