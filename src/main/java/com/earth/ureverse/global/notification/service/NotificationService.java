package com.earth.ureverse.global.notification.service;

import com.earth.ureverse.global.notification.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    void sendNotification(NotificationDto notification);

    int countUnread(Long userId);

    void sendReadUpdate(Long userId, List<Long> notificationIdList);
}
