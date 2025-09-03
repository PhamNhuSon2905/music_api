package com.music_backend_api.music_api.RequestUserDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {

    @NotBlank(message = "Tên người dùng không được để trống!")
    private String username;

    @NotBlank(message = "Địa chỉ email không được để trống!")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Địa chỉ email không hợp lệ!"
    )
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;

    private String avatar;

    @NotBlank(message = "Họ và tên không được để trống!")
    private String fullname;

    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại phải gồm đúng 10 chữ số!")
    private String phone;

    @NotNull(message = "Giới tính không được để trống!")
    private Boolean gender;
}
