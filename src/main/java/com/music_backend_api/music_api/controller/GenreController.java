package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
@Tag(name = "Genre API", description = "Quản lý chủ đề bài hát trong hệ thống")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Operation(summary = "Lấy tất cả các chủ đề bài hát", description = "Trả về thông tin các chủ đề bài hát")
    @GetMapping
    public List<Map<String, Object>> getAllGenres() {
        return genreService.getAllGenres();
    }

    @Operation(summary = "Lấy danh sách bài hát theo chủ đề đó", description = "Trả về thông tin bài hát tương ứng với ID chủ đề được cung cấp.")
    @GetMapping("/{id}/songs")
    public List<Map<String, Object>> getSongsByGenre(@PathVariable Long id) {
        return genreService.getSongsByGenre(id);
    }
}
