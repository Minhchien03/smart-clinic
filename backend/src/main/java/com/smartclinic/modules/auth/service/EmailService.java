package com.smartclinic.modules.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String token) {
        log.info("Bắt đầu chuẩn bị gửi email xác thực đến: {}", to);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setTo(to);
            message.setSubject("Xác thực tài khoản Smart Clinic");
            message.setText("Vui lòng click vào link để xác thực: http://localhost:8080/api/v1/auth/verify?token=" + token);
            
            log.debug("Nội dung email xác thực cho {}: {}", to, message.getText());

            mailSender.send(message);
            log.info("Hoàn tất gửi email xác thực thành công đến: {}", to);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email xác thực đến {}: {}", to, e.getMessage());
            throw e;
        }
    }
}
