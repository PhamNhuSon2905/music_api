package com.music_backend_api.music_api.DTO;

import com.music_backend_api.music_api.model.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String fullname;
    private String address;
    private String phone;
    private Boolean gender;


    public UserProfileDTO() {
    }


    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.fullname = user.getFullname();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.gender = user.getGender();
    }


}
