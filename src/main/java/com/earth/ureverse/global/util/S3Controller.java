package com.earth.ureverse.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/presigned-url")
    public S3Service.PresignedUploadResponse getPresignedUrl(@RequestParam("fileName") String fileName,
                                                             @RequestParam("contentType") String contentType) {
        log.info("fileName={}, contentType={}", fileName, contentType);
        return s3Service.generatePresignedUploadUrl(fileName, contentType);
    }
}
