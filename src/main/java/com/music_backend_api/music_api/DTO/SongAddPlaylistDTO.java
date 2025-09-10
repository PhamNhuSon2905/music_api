package com.music_backend_api.music_api.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SongAddPlaylistDTO {
    private String songId;
    private String title;
    private String artist;
    private String imageUrl;
    private boolean added;
    private LocalDateTime addedAt;

}
