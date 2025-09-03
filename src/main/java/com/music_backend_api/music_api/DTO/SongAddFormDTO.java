package com.music_backend_api.music_api.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class SongAddFormDTO {

    @NotBlank(message = "Tên bài hát không được để trống!")
    private String title;

    @NotBlank(message = "Tên ca sĩ không được để trống!")
    private String artist;

    @NotBlank(message = "Tên album không được để trống!")
    private String album;

    @NotNull(message = "Phải chọn file nhạc MP3!")
    private MultipartFile audioFile;

    @NotNull(message = "Phải chọn ảnh bài cho bài hát!")
    private MultipartFile imageFile;

    // Constructors
    public SongAddFormDTO() {
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public MultipartFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(MultipartFile audioFile) {
        this.audioFile = audioFile;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
