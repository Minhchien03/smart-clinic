package com.smartclinic.models.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

// Đánh dấu đây là Service trong Spring
@Service
// Annotation Lombok để tự động tạo constructor cho các thuộc tính final
// (JavaMailSender)
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    // Tiêm (inject) đối tượng JavaMailSender của Spring để thực hiện gửi mail
    private final JavaMailSender mailSender;

    // Hàm thực hiện gửi email chứa link xác thực
    public void sendVerificationEmail(String to, String token) {
        log.info("Bắt đầu chuẩn bị gửi email xác thực đến: {}", to);
        try {
            // Khởi tạo đối tượng tin nhắn email cơ bản
            SimpleMailMessage message = new SimpleMailMessage();

            // Cấu hình người nhận, tiêu đề và nội dung email
            message.setTo(to);
            message.setSubject("Xác thực tài khoản Smart Clinic");
            message.setText(
                    "Vui lòng click vào link để xác thực: http://localhost:8080/api/v1/auth/verify?token=" + token);

            log.debug("Nội dung email xác thực cho {}: {}", to, message.getText());

            // Thực thi gửi email
            mailSender.send(message);
            log.info("Hoàn tất gửi email xác thực thành công đến: {}", to);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email xác thực đến {}: {}", to, e.getMessage());
            throw e;
        }
    }
}