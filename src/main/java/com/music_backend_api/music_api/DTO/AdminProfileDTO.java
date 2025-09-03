package com.music_backend_api.music_api.DTO;

import com.music_backend_api.music_api.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminProfileDTO {

    @NotBlank(message = "Họ và tên không được để trống!")
    private String fullname;

    @NotBlank(message = "Địa chỉ không được để trống!")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại phải gồm đúng 10 chữ số!")
    private String phone;
    @NotNull(message = "Giới tính không được để trống!")
    private Boolean gender;

    private String avatar;

    public AdminProfileDTO(User user) {
        this.fullname = user.getFullname();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.gender = user.getGender();
        this.avatar = user.getAvatar();
    }
}
