package com.earth.ureverse.global.auth.controller;

import com.earth.ureverse.global.auth.service.EmailAuthService;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.net.URI;

@Tag(name = "Auth-Email", description = "회원가입 이메일 인증 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class EmailAuthController {

    private final EmailAuthService authService;

    @Value("${frontend.email-verify-redirect}")
    private String frontendRedirectUrl;

    @PostMapping("/send-verification")
    public CommonResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        authService.sendVerificationEmail(request.getEmail());
        return CommonResponseEntity.success("인증 메일이 발송되었습니다.");
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verifyEmailToken(@RequestParam String token) {
        boolean success = authService.verifyEmailToken(token);

        String status = success ? "success" : "failure";
        String redirectUrl = frontendRedirectUrl + "?status=" + status;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/check-email")
    public CommonResponseEntity<EmailCheckResponse> checkEmailDuplicate(@RequestParam String email) {
        boolean available = authService.isEmailAvailable(email);
        return CommonResponseEntity.success(new EmailCheckResponse(available));
    }

    public record EmailCheckResponse(boolean available) {}

    // DTO
    public static class EmailRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}