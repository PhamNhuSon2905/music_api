package com.music_backend_api.music_api.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongAddPlaylistDTO {
    private String songId;
    private String title;
    private String artist;
    private String imageUrl;
    private Integer trackOrder;
    private boolean added; // true nếu bài hát đã nằm trong playlist, false nếu chưa

}
