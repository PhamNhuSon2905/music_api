package com.music_backend_api.music_api.RequestUserDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class PlaylistRequest {

    @NotBlank(message = "Tên playlist không được để trống!")
    private String name;

    // Ảnh playlist (có thể optional nếu user không chọn ảnh)
    private MultipartFile imageFile;

    @NotNull(message = "UserId không được để trống!")
    private Long userId;

    // Constructors
    public PlaylistRequest() {
    }


}