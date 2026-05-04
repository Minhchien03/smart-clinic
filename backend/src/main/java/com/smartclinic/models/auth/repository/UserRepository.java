package com.smartclinic.repository;

// Import Entity User và các class của Spring Data JPA
import com.smartclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// Đánh dấu đây là một Repository để Spring Data JPA tự động tạo implementation
@Repository
// Interface này cung cấp các phương thức CRUD cơ bản cho thực thể User
public interface UserRepository extends JpaRepository<User, UUID> {
    
    // Tìm kiếm người dùng dựa trên email
    Optional<User> findByEmail(String email);
    
    // Tìm kiếm người dùng dựa trên mã token xác thực
    Optional<User> findByVerificationToken(String token);
}