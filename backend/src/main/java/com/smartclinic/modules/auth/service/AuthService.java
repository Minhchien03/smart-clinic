package com.smartclinic.modules.auth.service;

import com.smartclinic.modules.auth.dto.RegisterRequest;
import com.smartclinic.modules.auth.entity.User;
import com.smartclinic.modules.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public String register(RegisterRequest request) {
        log.info("Bắt đầu thực thi logic đăng ký cho email: {}", request.getEmail());
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Đăng ký thất bại: Email {} đã tồn tại trong hệ thống", request.getEmail());
            throw new RuntimeException("Email is already in use. Please choose another one.");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash("encoded_password_here"); 
        user.setRole("PATIENT");
        user.setActive(false); 

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        log.debug("Đã tạo token xác thực: {} cho email: {}", token, request.getEmail());

        userRepository.save(user);
        log.debug("Đã lưu user mới vào cơ sở dữ liệu: {}", user.getEmail());

        String verificationLink = "http://localhost:8080/api/v1/auth/verify?token=" + token;
        System.out.println("Gửi email tới: " + user.getEmail() + " | Link xác thực: " + verificationLink);
        log.info("Kết thúc quá trình đăng ký cho email: {}", request.getEmail());

        return "Đăng ký thành công. Vui lòng kiểm tra email để kích hoạt tài khoản!";
    }

    @Transactional
    public String verifyEmail(String token) {
        log.info("Bắt đầu thực thi logic xác thực email cho token: {}", token);
        
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> {
                    log.warn("Xác thực thất bại: Token {} không hợp lệ hoặc đã hết hạn", token);
                    return new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn");
                });

        log.debug("Tìm thấy user với token xác thực: {}", user.getEmail());

        user.setActive(true);
        user.setVerificationToken(null); 
        
        userRepository.save(user);
        log.debug("Đã cập nhật trạng thái active cho user: {}", user.getEmail());
        
        log.info("Hoàn tất xác thực email cho user: {}", user.getEmail());

        return "Xác thực email thành công! Bây giờ bạn có thể đăng nhập.";
    }
}
