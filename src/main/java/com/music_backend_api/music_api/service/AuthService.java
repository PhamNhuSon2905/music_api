package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.RequestUserDTO.UserLoginDTO;
import com.music_backend_api.music_api.RequestUserDTO.UserRegisterDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import com.music_backend_api.music_api.roles.Role;
import com.music_backend_api.music_api.security.JwtUtil;
import com.music_backend_api.music_api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public ResponseEntity<?> register(UserRegisterDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("Tên người dùng đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng!");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        String avatar = dto.getAvatar();
        if (avatar == null || avatar.trim().isEmpty()) {
            avatar = "avatar/default_user.png";
        }
        user.setAvatar(avatar);
        user.setFullname(dto.getFullname());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());
        user.setEnabled(true);

        userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    public ResponseEntity<?> login(UserLoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userPrincipal);

        User user = userPrincipal.getUser();
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("avatar", user.getAvatar());
        response.put("fullname", user.getFullname());
        response.put("lastLogin", user.getLastLogin());

        return ResponseEntity.ok(response);
    }

    // Ủy quyền cho TokenBlacklistService
    public void blacklistToken(String token, long expiry) {
        tokenBlacklistService.blacklistToken(token, expiry);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistService.isTokenBlacklisted(token);
    }
}
