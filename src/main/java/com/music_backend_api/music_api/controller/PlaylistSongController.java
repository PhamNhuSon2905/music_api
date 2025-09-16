package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.RequestUserDTO.PlaylistSongRequest;
import com.music_backend_api.music_api.service.PlaylistSongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/playlist-songs")
@Tag(name = "Playlist Song API", description = "Quản lý bài hát trong playlist")
public class PlaylistSongController {

    private final PlaylistSongService playlistSongService;

    public PlaylistSongController(PlaylistSongService playlistSongService) {
        this.playlistSongService = playlistSongService;
    }

    @Operation(
            summary = "Lấy danh sách bài hát trong playlist",
            description = "Trả về danh sách bài hát thuộc playlist kèm phân trang."
    )
    @GetMapping("/{playlistId}")
    public ResponseEntity<?> getSongsInPlaylist(
            @PathVariable Long playlistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return playlistSongService.getSongsInPlaylist(playlistId, page, size);
    }

    @Operation(
            summary = "Thêm bài hát vào playlist",
            description = "Thêm một bài hát vào playlist đã chọn."
    )
    @PostMapping
    public ResponseEntity<?> addSongToPlaylist(@RequestBody @Valid PlaylistSongRequest request) {
        return playlistSongService.addSongToPlaylist(request);
    }

    @Operation(
            summary = "Xóa bài hát khỏi playlist",
            description = "Xóa một bài hát ra khỏi playlist theo playlistId và songId."
    )
    @DeleteMapping("/{playlistId}/{songId}")
    public ResponseEntity<?> removeSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable String songId
    ) {
        return playlistSongService.removeSongFromPlaylist(playlistId, songId);
    }

    @Operation(
            summary = "Tìm kiếm bài hát trong playlist",
            description = "Tìm kiếm bài hát theo tên trong playlist"
    )
    @GetMapping("/{playlistId}/search")
    public ResponseEntity<?> searchSongsInPlaylist(
            @PathVariable Long playlistId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return playlistSongService.searchSongsInPlaylist(playlistId, keyword, page, size);
    }
}
