package com.earth.ureverse.global.notification.service;

import com.earth.ureverse.global.notification.dto.NotificationDto;

public interface NotificationService {
    void sendNotification(NotificationDto notification);

    int countUnread(Long userId);
}
