package com.smartclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.Arrays;
import java.util.List;

// Đánh dấu đây là class cấu hình của Spring Boot
@Configuration
// turn on Spring Security system (Bật tính năng bảo mật web)
@EnableWebSecurity
public class Security {
    
    // Khởi tạo một Bean trong Spring context để xử lý việc mã hóa mật khẩu
    @Bean
    // encrypt the password and compare the encrypted password with the one in the
    // database (Mã hóa mật khẩu và so sánh mật khẩu mã hóa với mật khẩu trong DB)
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Khởi tạo Bean định nghĩa các quy tắc bảo mật cho luồng HTTP request
    @Bean
    // define security rule (Định nghĩa luật bảo mật)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Vô hiệu hóa CSRF (bảo vệ chống giả mạo request liên trang) để đơn giản hóa khi phát triển
        http.csrf(csrf -> csrf.disable()) 
                // Kích hoạt CORS với cấu hình mặc định (sẽ dùng bean corsConfigurationSource bên dưới)
                .cors(Customizer.withDefaults())
                // Bắt đầu cấu hình quyền truy cập cho các request
                .authorizeHttpRequests(auth -> auth
                        // Cho phép truy cập vào endpoint /error (tránh lỗi 403 khi validation thất bại)
                        .requestMatchers("/error").permitAll()
                        // allow access to auth endpoints without authentication 
                        // (Cho phép truy cập vào các API bắt đầu bằng "/api/auth/" như đăng nhập, đăng ký mà không cần xác thực)
                        .requestMatchers(
                            "/api/v1/auth/**", 
                            "/api/auth/**", 
                            "/v3/api-docs/**", 
                            "/swagger-ui/**", 
                            "/swagger-ui.html"
                        ).permitAll()
                        // require authentication for any other request
                        // (Yêu cầu phải đăng nhập với TẤT CẢ các request khác)
                        .anyRequest().authenticated() 
                    
                )
                // Thêm cấu hình Stateless cho REST API (không dùng Session)
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // Build và trả về cấu hình bộ lọc bảo mật
        return http.build();
    }

    // Cấu hình CORS để cho phép frontend gọi API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}