package com.smartclinic.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

// Đánh dấu class này là một Entity được quản lý bởi JPA
@Entity
// Ánh xạ Entity này tới bảng có tên là "users" trong DB
@Table(name = "users")
// Tự động sinh getter/setter bằng Lombok
@Data
public class User {
    // Đánh dấu cột id là khóa chính của bảng
    @Id
    // Tự động phát sinh giá trị khóa chính theo định dạng UUID
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Cột email: giá trị phải là duy nhất (unique) và không được phép null
    @Column(unique = true, nullable = false)
    private String email;

    // Cột chứa mật khẩu đã được mã hóa, tên cột trong database là "password_hash"
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Cột vai trò của user, mặc định khi tạo mới là "PATIENT" (Bệnh nhân)
    @Column(nullable = false)
    private String role = "PATIENT";

    // Cột trạng thái kích hoạt tài khoản, mặc định là false (chưa kích hoạt)
    @Column(name = "is_active")
    private boolean isActive = false;

    // Cột chứa mã xác thực (dùng để gửi link trong email kích hoạt)
    @Column(name = "verification_token")
    private String verificationToken;
}