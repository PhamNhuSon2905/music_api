package com.music_backend_api.music_api.RequestUserDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @NotBlank(message = "Vui lòng nhập tên người dùng!")
    private String username;

    @NotBlank(message = "Vui lòng nhập mật khẩu!")
    private String password;
}
