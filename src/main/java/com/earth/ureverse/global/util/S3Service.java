package com.earth.ureverse.global.util;

import com.earth.ureverse.member.mapper.ProductImageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class S3Service {

    private final S3Presigner s3Presigner;

    private final RedisImageService redisImageService;

    private final S3Client s3Client;

    private ProductImageMapper productImageMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Service(S3Presigner s3Presigner, S3Client s3Client, RedisImageService redisImageService, ProductImageMapper productImageMapper) {
        this.s3Presigner = s3Presigner;
        this.s3Client = s3Client;
        this.redisImageService = redisImageService;
        this.productImageMapper = productImageMapper;
    }

    public PresignedUploadResponse generatePresignedUploadUrl(String originalFilename, String contentType) {
        String key = "uploads/" + UUID.randomUUID() + "-" + originalFilename;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        URL presignedUrl = s3Presigner.presignPutObject(presignRequest).url();
        String accessUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        // Redis에 임시 저장 (예: 24시간 TTL)
        redisImageService.saveTempImage(key, accessUrl, Duration.ofHours(24));

        return new PresignedUploadResponse(presignedUrl.toString(), key, accessUrl);
    }

    public record PresignedUploadResponse(String presignedUrl, String key, String accessUrl) {}

    public void deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        log.info("S3 object deleted: {}", key);
    }

}
