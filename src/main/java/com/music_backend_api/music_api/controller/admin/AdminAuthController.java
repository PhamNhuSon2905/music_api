package com.music_backend_api.music_api.controller.admin;

import com.music_backend_api.music_api.DTO.AdminLoginDTO;
import com.music_backend_api.music_api.DTO.AdminRegisterDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import com.music_backend_api.music_api.roles.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/auth")
public class AdminAuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Trang đăng nhập (GET)
    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object error = request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error.toString());
            request.getSession().removeAttribute("error");
        }

        model.addAttribute("adminLoginDTO", new AdminLoginDTO());
        redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
        return "admin/auth/login";
    }


    // Trang đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("adminRegisterDTO", new AdminRegisterDTO());
        return "admin/auth/register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute("adminRegisterDTO") @Valid AdminRegisterDTO dto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            result.rejectValue("email", "error.adminRegisterDTO", "Email đã tồn tại!");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            result.rejectValue("username", "error.adminRegisterDTO", "Tên người dùng đã tồn tại!");
        }

        if (result.hasErrors()) {
            return "admin/auth/register";
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAvatar(dto.getAvatar());
        user.setRole(Role.ADMIN);
        user.setFullname(dto.getFullname());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());


        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập.");

        return "redirect:/admin/auth/login";
    }

}
