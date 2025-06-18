package com.earth.ureverse.admin.service;

import com.earth.ureverse.global.common.exception.UpdateStatusException;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.util.NotificationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductStatusAsyncService {

    private final ProductMapper productMapper;
    private final NotificationHelper notificationHelper;

    //비동기 실행
    @Async
    public void updateStatusWithDelay(Long productId, Long adminId){
        try{
            Thread.sleep(5000);
            updateStatus(productId, "DELIVERING", adminId);
            Thread.sleep(5000);
            updateStatus(productId, "FINISH", adminId);
        } catch (InterruptedException e) {
            throw new UpdateStatusException("상태 업데이트 중 오류가 발생했습니다.");
        }
    }

    //상태 업데이트
    public void updateStatus(Long productId, String status, Long updatedBy) {
        productMapper.updateProductStatus(productId, status, LocalDateTime.now(), updatedBy);

        notificationHelper.updateNotification(productId, status, status, updatedBy);
    }
}
