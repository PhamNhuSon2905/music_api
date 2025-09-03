package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/songs")
@Tag(name = "Song API", description = "Quản lý bài hát trong hệ thống")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Operation(summary = "Lấy tất cả bài hát", description = "Trả về danh sách toàn bộ bài hát hiện có trong hệ thống.")
    @GetMapping
    public Map<String, List<Song>> getAll() {
        return songService.getAllSongs();
    }

    @Operation(summary = "Lấy bài hát theo ID", description = "Trả về thông tin bài hát tương ứng với ID được cung cấp.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return songService.getSongById(id);
    }

    @Operation(summary = "Tìm kiếm bài hát theo tiêu đề, ca sĩ hoặc album")
    @GetMapping("/search")
    public Map<String, List<Song>> search(@RequestParam String keyword) {
        return Collections.singletonMap("songs", songService.searchSongs(keyword));
    }


}
