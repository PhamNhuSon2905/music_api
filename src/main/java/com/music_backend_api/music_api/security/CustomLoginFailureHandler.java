package com.music_backend_api.music_api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ((username == null || username.trim().isEmpty()) &&
                (password == null || password.trim().isEmpty())) {
            request.getSession().setAttribute("error", "Vui lòng nhập tên người dùng và mật khẩu!");
        } else if (username == null || username.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng nhập tên người dùng!");
        } else if (password == null || password.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng nhập mật khẩu!");
        } else if (exception instanceof BadCredentialsException) {
            request.getSession().setAttribute("error", "Tên đăng nhập và mật khẩu không chính xác!");
        } else {
            request.getSession().setAttribute("error", "Đăng nhập thất bại!");
        }

        response.sendRedirect("/admin/auth/login?error=true");
    }


}
