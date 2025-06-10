package com.earth.ureverse.member.service;

import com.earth.ureverse.global.util.RedisService;
import com.earth.ureverse.global.util.S3Service;
import com.earth.ureverse.member.mapper.ProductImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCleanupService {

    private final S3Service s3Service;
    private final RedisService redisService;
    private final ProductImageMapper productImageMapper;

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시
    public void cleanOrphanImages() {
        Set<String> keys = redisService.getAllImageKeys();
        log.info("keys: {}", keys);
        for (String key : keys) {
            String url = redisService.getAccessUrl(key);
            log.info("accessUrl: {}", url);
            if (!productImageMapper.existsByUrl(url)) {
                s3Service.deleteObject(key);
                redisService.deleteKey(key);
                log.info("Deleted orphan image: {}", key);
            }
        }
    }
}
