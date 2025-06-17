package com.earth.ureverse.member.service;

import com.earth.ureverse.global.util.RedisImageService;
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
    private final RedisImageService redisImageService;
    private final ProductImageMapper productImageMapper;

    @Scheduled(cron = "0 0 4 * * *") // 매일 새벽 4시
    public void cleanOrphanImages() {
        Set<String> keys = redisImageService.getAllImageKeys();
        log.info("keys: {}", keys);
        for (String key : keys) {
            String url = redisImageService.getAccessUrl(key);
            log.info("accessUrl: {}", url);
            if (!productImageMapper.existsByUrl(url)) {
                s3Service.deleteObject(key);
                redisImageService.deleteKey(key);
                log.info("Deleted orphan image: {}", key);
            }
        }
    }
}
