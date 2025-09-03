package com.music_backend_api.music_api.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GenreAssignSongDTO {
    private Long genreId;
    private List<String> songIds;
}
