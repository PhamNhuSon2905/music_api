package com.music_backend_api.music_api.security;

import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import java.io.IOException;
import java.sql.Timestamp;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomLoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Gửi flash message khi đăng nhập
        FlashMap flashMap = new FlashMap();
        flashMap.put("success", "Đăng nhập thành công!");
        FlashMapManager flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        // Cập nhật thời gian đăng nhập cuối

        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);

        // Chuyển đến dashboard
        response.sendRedirect("/admin/dashboard/index");
    }
}
