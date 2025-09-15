package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.RequestUserDTO.PlaylistRequest;
import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/playlists")
@Tag(name = "Playlist API", description = "Quản lý playlist trong hệ thống")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @Operation(summary = "Lấy tất cả playlist của user", description = "Trả về danh sách playlist của 1 user với phân trang.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return playlistService.getAllPlaylistsByUser(userId, page, size);
    }

    @Operation(summary = "Lấy playlist theo ID", description = "Trả về thông tin playlist tương ứng với ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return playlistService.getPlaylistById(id);
    }

    @Operation(summary = "Tạo playlist mới", description = "Tạo một playlist mới kèm ảnh (tuỳ chọn).")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute @Valid PlaylistRequest request) {
        return playlistService.createPlaylist(request);
    }

    @Operation(summary = "Cập nhật playlist", description = "Cập nhật thông tin playlist, có thể thay đổi tên và ảnh.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @ModelAttribute @Valid PlaylistRequest request,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        try {
            Playlist updated = playlistService.updatePlaylist(id, request, imageFile);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cập nhật playlist thành công!");
            response.put("playlist", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @Operation(summary = "Xoá playlist", description = "Xoá playlist theo ID, đồng thời xoá các bài hát trong đó.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return playlistService.deletePlaylist(id);
    }

    @Operation(summary = "Tìm playlist theo tên", description = "Tìm playlist theo tên trong danh sách playlist của user.")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return playlistService.searchPlaylists(userId, keyword, page, size);
    }
}
