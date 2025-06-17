package com.earth.ureverse.member.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationQueryDto {
    private Long userId;
    private String notificationType; // AI_RESULT, FINAL_INSPECTION
    private String title;
    private String message;
    private String isRead; // Y, N

    @Builder
    public NotificationQueryDto(Long userId, String notificationType, String title, String message, String isRead) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
    }
}
