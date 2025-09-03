package com.music_backend_api.music_api.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenreAddFormDTO {
    @NotBlank(message = "Tên thể loại không được để trống!")
    private String name;
    @NotBlank(message = "Mô tả thể loại không được để trống!")
    private String description;

}
