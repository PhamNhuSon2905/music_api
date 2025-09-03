package com.music_backend_api.music_api.DTO;

import com.music_backend_api.music_api.model.FavoriteSong;
import com.music_backend_api.music_api.model.Song;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserFavoriteSongDTO {
    private String id;
    private String title;
    private String album;
    private String artist;
    private String source;
    private String image;
    private int duration;
    private LocalDateTime createdAt;

    public UserFavoriteSongDTO(FavoriteSong fav) {
        Song song = fav.getSong();
        this.id = song.getId();
        this.title = song.getTitle();
        this.album = song.getAlbum();
        this.artist = song.getArtist();
        this.source = song.getSource();
        this.image = song.getImage();
        this.duration = song.getDuration();
        this.createdAt = fav.getCreatedAt();
    }
}
