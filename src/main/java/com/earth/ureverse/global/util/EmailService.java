package com.earth.ureverse.global.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }

    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "[U:Reverse] 이메일 인증 요청";
        String htmlContent = "<p>아래 링크를 클릭하여 이메일 인증을 완료해주세요:</p>"
                + "<p><a href=\"" + verificationLink + "\">이메일 인증하기</a></p>"
                + "<p>해당 링크는 <strong>30분 후 만료</strong>됩니다.</p>";
        sendHtmlMessage(to, subject, htmlContent);
    }

    public void sendTempPasswordEmail(String to, String tempPassword) {
        String subject = "[U:Reverse] 임시 비밀번호 발송";
        String htmlContent = "<p>임시 비밀번호: <strong>" + tempPassword + "</strong></p>"
                + "<p>로그인 후 반드시 비밀번호를 변경해주세요.</p>";
        sendHtmlMessage(to, subject, htmlContent);
    }
}