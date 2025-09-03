package com.music_backend_api.music_api.RequestUserDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteSongRequest {
    private Long userId;
    private String songId;
}
