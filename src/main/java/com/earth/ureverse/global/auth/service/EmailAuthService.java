package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.util.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailAuthService {

    private final RedisEmailTokenService redisService;

    private final EmailService emailService;

    private final AuthMapper authMapper;

    @Value("${email.server-url}")
    private String url;

    public void sendVerificationEmail(String email) {
        String token = UUID.randomUUID().toString();
        redisService.saveToken(token, email, Duration.ofMinutes(30)); // 30분 유효

        String verifyUrl = url + "/api/v1/auth/verify?token=" + token;

        emailService.sendVerificationEmail(email, verifyUrl);
        log.info("📧 인증 메일 발송됨: " + verifyUrl);
    }

    public boolean verifyEmailToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        String email = redisService.getEmailByToken(token);
        if (email == null) {
            return false; // 토큰 없거나 만료됨
        }

        redisService.deleteToken(token); // 재사용 방지
        return true;
    }

    public boolean isEmailAvailable(String email) {
        return !authMapper.existsByEmail(email);
    }
}
