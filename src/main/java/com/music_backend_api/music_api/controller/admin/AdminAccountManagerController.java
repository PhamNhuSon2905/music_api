package com.music_backend_api.music_api.controller.admin;

import com.music_backend_api.music_api.DTO.AdminAccountUpdateDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/account")
public class AdminAccountManagerController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/management")
    public String accountList(Model model,
                              @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "5") int pageSize,
                              @RequestParam(defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "") String filterBy,
                              @RequestParam(required = false) String role) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").descending());

        Specification<User> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (keyword != null && !keyword.isBlank()) {
                if ("username".equals(filterBy)) {
                    p = cb.and(p, cb.like(cb.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"));
                } else if ("email".equals(filterBy)) {
                    p = cb.and(p, cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"));
                }
            }

            if (role != null && !role.isBlank()) {
                p = cb.and(p, cb.equal(root.get("role").as(String.class), role));
            }

            return p;
        };

        Page<User> page = userRepository.findAll(spec, pageable);

        model.addAttribute("users", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("filterBy", filterBy);
        model.addAttribute("role", role);

        return "admin/account/index";
    }

    @GetMapping("/detail/{id}")
    public String accountDetail(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với mã tài khoản: " + id));


        AdminAccountUpdateDTO dto = new AdminAccountUpdateDTO();
        dto.setFullname(user.getFullname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setGender(user.getGender());
        dto.setEnabled(user.getEnabled());
        dto.setRole(user.getRole());

        model.addAttribute("user", user);
        model.addAttribute("adminAccountUpdateDTO", dto);
        model.addAttribute("userId", user.getId());

        return "admin/account/detail";
    }


    @GetMapping("/edit/{id}")
    public String editAccountForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với id: " + id));

        AdminAccountUpdateDTO dto = new AdminAccountUpdateDTO();
        dto.setFullname(user.getFullname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setGender(user.getGender());
        dto.setEnabled(user.getEnabled());
        dto.setRole(user.getRole());

        model.addAttribute("adminAccountUpdateDTO", dto);
        model.addAttribute("userId", id);

        return "admin/account/edit";
    }


    @PostMapping("/edit/{id}")
    public String updateAccount(@PathVariable Long id,
                                @Valid @ModelAttribute("adminAccountUpdateDTO") AdminAccountUpdateDTO dto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("adminAccountUpdateDTO", dto);
            return "admin/account/edit";
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với id: " + id));

        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());
        user.setEnabled(dto.getEnabled());
        user.setRole(dto.getRole());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật tài khoản thành công!");
        return "redirect:/admin/account/management";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        // Lấy tên người dùng hiện tại từ SecurityContext
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // 👉 Lấy Optional<User> và kiểm tra
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElse(null);  // hoặc .orElseThrow(...) nếu muốn bắt buộc

        if (currentUser != null && currentUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không thể xoá tài khoản đang đăng nhập.");
            return "redirect:/admin/account/management";
        }

        try {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xoá tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xoá tài khoản: " + e.getMessage());
        }

        return "redirect:/admin/account/management";
    }


}
