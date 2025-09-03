package com.music_backend_api.music_api.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDTO {

    @NotBlank(message = "Vui lòng nhập tên người dùng để đăng nhập!")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu để đăng nhập!")
    private String password;
}
