package com.earth.ureverse.member.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationQueryDto {
    private Long userId;
    private String notificationType; // AI_RESULT, FINAL_INSPECTION
    private String title;
    private String message;
    private String isRead; // Y, N
    private LocalDateTime timestamp;
    private Long createUserId;

    @Builder
    public NotificationQueryDto(Long userId, String notificationType, String title, String message, String isRead, LocalDateTime timestamp, Long createUserId) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.timestamp = timestamp;
        this.createUserId = createUserId;
    }
}
