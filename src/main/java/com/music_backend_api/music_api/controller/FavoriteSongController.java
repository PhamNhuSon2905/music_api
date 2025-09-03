package com.music_backend_api.music_api.controller;

import com.music_backend_api.music_api.DTO.UserFavoriteSongDTO;
import com.music_backend_api.music_api.RequestUserDTO.FavoriteSongRequest;
import com.music_backend_api.music_api.service.FavoriteSongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite API", description = "Quản lý bài hát yêu thích")
public class FavoriteSongController {

    @Autowired
    private FavoriteSongService favoriteSongService;

    @Operation(summary = "Lấy danh sách bài hát yêu thích của user")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFavoriteSongDTO>> getFavorites(@PathVariable Long userId) {
        List<UserFavoriteSongDTO> favorites = favoriteSongService.getFavoriteSongsByUser(userId);
        return ResponseEntity.ok(favorites);
    }

    @Operation(summary = "Thêm bài hát vào danh sách yêu thích")
    @PostMapping("/add")
    public ResponseEntity<Void> addFavorite(@RequestBody FavoriteSongRequest request) {
        favoriteSongService.addFavoriteSong(request.getUserId(), request.getSongId());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Xoá bài hát khỏi danh sách yêu thích")
    @PostMapping("/remove")
    public ResponseEntity<Void> removeFavorite(@RequestBody FavoriteSongRequest request) {
        favoriteSongService.removeFavoriteSong(request.getUserId(), request.getSongId());
        return ResponseEntity.ok().build();
    }

}
