package com.music_backend_api.music_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreEditFormDTO {

    @NotNull(message = "ID không được để trống")
    private Long id;

    @NotBlank(message = "Tên thể loại không được để trống!")
    @Size(max = 255, message = "Tên thể loại không được vượt quá 255 ký tự")
    private String name;

    @NotBlank(message = "Mô tả thể loại không được để trống!")
    @Size(max = 1000, message = "Mô tả thể loại không được vượt quá 1000 ký tự")
    private String description;
}
