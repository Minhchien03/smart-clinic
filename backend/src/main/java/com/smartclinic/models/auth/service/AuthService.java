package com.smartclinic.service;

import com.smartclinic.dto.RegisterRequest;
import com.smartclinic.entity.User;
import com.smartclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

// Đánh dấu đây là một Service component xử lý logic trong Spring
@Service
@Slf4j
public class AuthService {

    // Khai báo UserRepository để gọi các hàm thao tác với DB
    private final UserRepository userRepository;
    // Inject PasswordEncoder & EmailService

    // Constructor tự động inject UserRepository vào Service này
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Đánh dấu hàm này thực thi trong một Transaction (nếu có lỗi DB sẽ tự động rollback)
    @Transactional
    public String register(RegisterRequest request) {
        log.info("Bắt đầu thực thi logic đăng ký cho email: {}", request.getEmail());
        
        // check email exist (Kiểm tra xem email đã tồn tại trong database chưa)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Đăng ký thất bại: Email {} đã tồn tại trong hệ thống", request.getEmail());
            throw new RuntimeException("Email is already in use. Please choose another one.");
        }
        
        // create user (Khởi tạo đối tượng User mới và gán dữ liệu)
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash("encoded_password_here"); 
        user.setRole("PATIENT");
        user.setActive(false); 

        // create verification token (Tạo ra một chuỗi mã UUID ngẫu nhiên làm token xác thực)
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        log.debug("Đã tạo token xác thực: {} cho email: {}", token, request.getEmail());

        // Lưu thông tin user vừa tạo vào database
        userRepository.save(user);
        log.debug("Đã lưu user mới vào cơ sở dữ liệu: {}", user.getEmail());

        // 4. Gửi Email (Giả lập việc khởi tạo nội dung link xác thực)
        String verificationLink = "http://localhost:8080/api/v1/auth/verify?token=" + token;
        // Tạm thời in ra console nội dung sẽ gửi
        System.out.println("Gửi email tới: " + user.getEmail() + " | Link xác thực: " + verificationLink);
        log.info("Kết thúc quá trình đăng ký cho email: {}", request.getEmail());

        return "Đăng ký thành công. Vui lòng kiểm tra email để kích hoạt tài khoản!";
    }

    // Logic xử lý khi người dùng click vào link xác thực trong email
    @Transactional
    public String verifyEmail(String token) {
        log.info("Bắt đầu thực thi logic xác thực email cho token: {}", token);
        
        // Tìm user trong database dựa trên mã token gửi qua đường dẫn
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> {
                    log.warn("Xác thực thất bại: Token {} không hợp lệ hoặc đã hết hạn", token);
                    return new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn");
                });

        log.debug("Tìm thấy user với token xác thực: {}", user.getEmail());

        // Kích hoạt tài khoản (Đổi trạng thái active thành true)
        user.setActive(true);
        user.setVerificationToken(null); // Xóa token sau khi dùng xong để tránh tái sử dụng
        
        // Lưu lại user với trạng thái mới vào database
        userRepository.save(user);
        log.debug("Đã cập nhật trạng thái active cho user: {}", user.getEmail());
        
        log.info("Hoàn tất xác thực email cho user: {}", user.getEmail());

        return "Xác thực email thành công! Bây giờ bạn có thể đăng nhập.";
    }
}