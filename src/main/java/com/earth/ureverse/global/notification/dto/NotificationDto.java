package com.earth.ureverse.global.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private Long notificationId;
    private Long userId;
    private String title;
    private String message;
    private String notificationType;
    private String isRead;
    private LocalDateTime createdAt;
}
