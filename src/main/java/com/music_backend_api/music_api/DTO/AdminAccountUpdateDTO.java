package com.music_backend_api.music_api.DTO;

import com.music_backend_api.music_api.roles.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminAccountUpdateDTO {

    @NotBlank(message = "Họ và tên không được để trống!")
    private String fullname;

    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Email không hợp lệ. Vui lòng thử lại")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Số điện thoại phải gồm 10 chữ số!")
    @NotBlank(message = "Số điện thoại không được để trống!")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;

    @NotNull(message = "Giới tính không được để trống!")
    private Boolean gender;


    @NotNull(message = "Trạng thái  tài khoản không được để trống!")
    private Boolean enabled;

    @NotNull(message = "Vai trò không được để trống")
    private Role role;


}
