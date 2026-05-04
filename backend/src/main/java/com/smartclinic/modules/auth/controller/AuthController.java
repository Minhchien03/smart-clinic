package com.smartclinic.modules.auth.controller;

import com.smartclinic.modules.auth.dto.RegisterRequest;
import com.smartclinic.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Bắt đầu xử lý đăng ký cho email: {}", request.getEmail());
        try {
            String result = authService.register(request);
            log.debug("Kết quả xử lý đăng ký từ AuthService: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình đăng ký cho email {}: {}", request.getEmail(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        log.info("Bắt đầu xử lý xác thực email với token: {}", token);
        try {
            String result = authService.verifyEmail(token);
            log.debug("Kết quả xử lý xác thực từ AuthService: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Lỗi xảy ra trong quá trình xác thực token {}: {}", token, e.getMessage());
            throw e;
        }
    }
}
