package com.music_backend_api.music_api.DTO;

import lombok.Getter;
import lombok.Setter;

public class UserUpdateProfileDTO {
    @Getter
    @Setter
    public static class UpdateUserProfileDTO {
        private String fullname;
        private String address;
        private String phone;
        private Boolean gender;
    }
}
