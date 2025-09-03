package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.Utils.Mp3Utils;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.repository.SongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Value("${upload.music-dir}")
    private String uploadMusicDir;

    @Value("${upload.image-dir}")
    private String uploadImageDir;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Map<String, List<Song>> getAllSongs() {
        return Collections.singletonMap("songs", songRepository.findAll());
    }

    public ResponseEntity<?> getSongById(String id) {
        return songRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy bài hát")));
    }


    public List<Song> searchSongs(String keyword) {
        return songRepository.searchByKeyword(keyword);
    }


}
