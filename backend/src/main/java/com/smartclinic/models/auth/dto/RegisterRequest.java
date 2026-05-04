package com.smartclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// Annotation của thư viện Lombok tự động tạo các hàm getter, setter
@Data
public class RegisterRequest {
    
    // Kiểm tra dữ liệu: Đảm bảo trường email không bị bỏ trống
    @NotBlank(message = "Email can't be blank")
    // Regex check validate: name + @ + domain + .com/.vn...
    // (Kiểm tra định dạng email bằng Regex để đảm bảo email hợp lệ)
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", 
             message = "Email is not in valid format.")
    private String email;

    // Kiểm tra dữ liệu: Đảm bảo trường password không bị bỏ trống
    @NotBlank(message = "Password can't be blank")
    private String password;

    // Kiểm tra dữ liệu: Đảm bảo trường họ tên không bị bỏ trống
    @NotBlank(message = "Full name can't be blank")
    private String fullName;
}