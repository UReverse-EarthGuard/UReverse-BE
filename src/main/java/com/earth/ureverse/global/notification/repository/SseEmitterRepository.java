package com.earth.ureverse.global.notification.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterRepository {
    // 메모리에 사용자 별 emitter 저장
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 연결 종료시 emitter 자동 삭제
    public SseEmitter save(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        return emitter;
    }

    public Optional<SseEmitter> get(Long userId) {
        return Optional.ofNullable(emitters.get(userId));
    }

    public void send(Long userId, SseEmitter.SseEventBuilder event) {
        Optional<SseEmitter> emitter = get(userId);
        emitter.ifPresent(e -> {
            try {
                e.send(event);
            } catch (IOException ex) {
                e.completeWithError(ex);
                emitters.remove(userId);
            }
        });
    }

}
