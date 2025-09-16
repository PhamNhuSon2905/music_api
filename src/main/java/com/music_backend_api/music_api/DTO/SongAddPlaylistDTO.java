package com.music_backend_api.music_api.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor  // Tạo constructor rỗng
@AllArgsConstructor // Nếu muốn có constructor đầy đủ field
public class SongAddPlaylistDTO {
    private String songId;
    private String title;
    private String album;
    private String artist;
    private String source;
    private String image;
    private int duration;
    private LocalDateTime createdAt;

    private boolean added;
    private LocalDateTime addedAt;
}
