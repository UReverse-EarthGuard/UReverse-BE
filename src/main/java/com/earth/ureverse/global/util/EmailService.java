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

    public void sendNotificationEmail(String to, String title, String message) {
        String emailTemplate = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>[U:Reverse] 알림</title>
        </head>
        <body style="font-family: 'Arial', sans-serif; background-color: #f9f9f9; padding: 20px;">
    
            <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; border: 1px solid #ddd; padding: 30px;">
                <h2 style="color: #333333; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                    🛎️ {{title}}
                </h2>
    
                <p style="font-size: 16px; color: #444444; line-height: 1.6;">
                    안녕하세요, 고객님.<br><br>
                    {{message}}
                </p>
    
                <div style="margin-top: 30px; text-align: center;">
                    <a href="https://ureverse-fe-member.vercel.app/" style="background-color: #3C8DBC; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;">
                        서비스 바로가기
                    </a>
                </div>
    
                <p style="font-size: 12px; color: #888888; margin-top: 40px; border-top: 1px solid #eee; padding-top: 10px;">
                    본 메일은 발신전용입니다.<br>
                </p>
            </div>
    
        </body>
        </html>
        """;

        String subject = "[U:Reverse] " + title;
        String htmlContent = emailTemplate
                .replace("{{title}}", title)
                .replace("{{message}}", message);

        sendHtmlMessage(to, subject, htmlContent);
    }
}