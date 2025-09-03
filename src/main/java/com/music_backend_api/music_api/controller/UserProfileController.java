package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.DTO.UserProfileDTO;
import com.music_backend_api.music_api.DTO.UserUpdateProfileDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.security.JwtUtil;
import com.music_backend_api.music_api.security.UserPrincipal;
import com.music_backend_api.music_api.service.AuthService;
import com.music_backend_api.music_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Profile API", description = "Quản lý thông tin cá nhân người dùng trong hệ thống")
public class UserProfileController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Lấy thông tin cá nhân của người dùng", description = "Trả về thông tin người dùng đang đăng nhập.")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        return ResponseEntity.ok(new UserProfileDTO(user));
    }

    @Operation(summary = "Cập nhật thông tin cá nhân của người dùng", description = "Cập nhật thông tin khi người dùng muốn chỉnh sửa thông tin cá nhân.")
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody UserUpdateProfileDTO.UpdateUserProfileDTO dto
    ) {
        User user = userPrincipal.getUser();
        User updated = userService.updateUserProfile(user.getId(), dto);
        return ResponseEntity.ok(new UserProfileDTO(updated));
    }


    @Operation(summary = "Tải ảnh đại diện lên cho người dùng")
    @PostMapping(value = "/avatar", consumes = {"multipart/form-data"})
    public ResponseEntity<UserProfileDTO> uploadAvatar(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestPart("file") MultipartFile file
    ) {
        User user = userPrincipal.getUser();
        User updated = userService.uploadUserAvatar(user.getId(), file);
        return ResponseEntity.ok(new UserProfileDTO(updated));
    }

    @Operation(summary = "Đăng xuất tài khoản hiện tại")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Không tìm thấy token.");
        }

        String token = authHeader.substring(7);

        long expiry = jwtUtil.getExpiryFromToken(token);
        authService.blacklistToken(token, expiry);

        return ResponseEntity.ok("Logout success");
    }


}

