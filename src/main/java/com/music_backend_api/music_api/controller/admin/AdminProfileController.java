package com.music_backend_api.music_api.controller.admin;

import com.music_backend_api.music_api.DTO.AdminProfileDTO;
import com.music_backend_api.music_api.DTO.ChangePasswordDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import com.music_backend_api.music_api.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.avatar-dir}")
    private String avatarUploadDir;

    @GetMapping
    public String showProfile(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại thông tin hồ sơ này!"));

        if (user.getAvatar() == null || user.getAvatar().trim().isEmpty()) {
            user.setAvatar("/assets/images/user2-160x160.jpg");
        }
        model.addAttribute("user", user);
        return "admin/profile/index";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại thông tin hồ sơ này!"));
        model.addAttribute("adminProfileDTO", new AdminProfileDTO(user));
        return "admin/profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @Valid @ModelAttribute("adminProfileDTO") AdminProfileDTO dto,
            BindingResult result,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/profile/edit";
        }

        User user = userRepository.findById(userPrincipal.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại thông tin hồ sơ này!"));

        user.setFullname(dto.getFullname());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());

        if (!avatarFile.isEmpty()) {
            try {
                String projectDir = System.getProperty("user.dir");
                Path fullPath = Paths.get(projectDir, avatarUploadDir);
                Files.createDirectories(fullPath);

                if (user.getAvatar() != null && !user.getAvatar().equals("/assets/images/user2-160x160.jpg")) {
                    Path oldAvatarPath = fullPath.resolve(Paths.get(user.getAvatar()).getFileName().toString());
                    if (Files.exists(oldAvatarPath)) {
                        Files.delete(oldAvatarPath);
                    }
                }
                String filename = "avatar_" + user.getId() + "_" + avatarFile.getOriginalFilename();
                Path filePath = fullPath.resolve(filename);
                avatarFile.transferTo(filePath.toFile());

                user.setAvatar("/avatar/" + filename);
            } catch (IOException e) {
                result.rejectValue("avatar", null, "Lỗi khi lưu ảnh đại diện!: " + e.getMessage());
                return "admin/profile/edit";
            }
        }
        userRepository.save(user);
        UserPrincipal.updateAuthentication(user);
        redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        return "redirect:/admin/profile";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        return "admin/profile/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO dto,
            BindingResult result,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            RedirectAttributes redirectAttributes,
            Model model, HttpServletRequest request
    ) {
        if (result.hasErrors()) {
            return "admin/profile/change-password";
        }

        User user = userRepository.findById(userPrincipal.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            model.addAttribute("passwordError", "Mật khẩu hiện tại không đúng!");
            return "admin/profile/change-password";
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("passwordError", "Mật khẩu xác nhận không khớp!");
            return "admin/profile/change-password";
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        request.getSession().invalidate();

        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại!");
        return "redirect:/admin/auth/login";


    }


}
