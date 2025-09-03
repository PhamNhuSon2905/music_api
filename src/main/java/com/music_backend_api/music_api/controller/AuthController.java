package com.music_backend_api.music_api.controller;


import com.music_backend_api.music_api.RequestUserDTO.UserLoginDTO;
import com.music_backend_api.music_api.RequestUserDTO.UserRegisterDTO;
import com.music_backend_api.music_api.repository.UserRepository;
import com.music_backend_api.music_api.security.JwtUtil;
import com.music_backend_api.music_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "User API", description = "Quản lý xác thực người dùng trong hệ thống")
public class AuthController {


    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Đăng ký tài khoản người dùng", description = "Đăng ký các thông tin để tạo tài khoản người dùng")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO dto) {
        return authService.register(dto);
    }

    @Operation(summary = "Đăng nhập tài khoản người dùng", description = "Sử dụng tài khoản và mật khẩu để đăng nhập")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        return authService.login(dto);
    }
}
