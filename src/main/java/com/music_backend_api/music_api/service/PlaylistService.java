package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.RequestUserDTO.PlaylistRequest;
import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.PlaylistRepository;
import com.music_backend_api.music_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    @Value("${upload.image-playlist-dir}")
    private String uploadImagePlaylistDir;

    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    // Lấy tất cả playlist của user (phân trang)
    public ResponseEntity<?> getAllPlaylistsByUser(Long userId, int page, int size) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy user"));
        }

        Page<Playlist> playlists = playlistRepository.findByUser(userOpt.get(), PageRequest.of(page, size));
        return ResponseEntity.ok(Map.of("playlists", playlists.getContent(),
                "total", playlists.getTotalElements()));
    }

    // Lấy playlist theo id
    public ResponseEntity<?> getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Không tìm thấy playlist")));
    }

    // Tạo playlist mới
    // Tạo playlist mới
    public ResponseEntity<?> createPlaylist(PlaylistRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy user!"));
        }

        User user = userOpt.get();

        // Kiểm tra trùng tên playlist trong cùng user
        if (playlistRepository.existsByUserAndName(user, request.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Tên playlist đã tồn tại!"));
        }

        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setUser(user);
        playlist.setCreatedAt(LocalDateTime.now());

        MultipartFile imageFile = request.getImageFile();

        // Upload ảnh nếu có
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageDir = new File(uploadImagePlaylistDir).getAbsolutePath();

                String fileName = UUID.randomUUID().toString().substring(0, 10)
                        + "_" + imageFile.getOriginalFilename();

                File savedImage = new File(imageDir + "/" + fileName);
                savedImage.getParentFile().mkdirs();
                imageFile.transferTo(savedImage);

                // Lưu đường dẫn ảnh
                playlist.setImage("/imagePlaylist/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Không thể lưu ảnh playlist: " + e.getMessage()));
            }
        }

        Playlist saved = playlistRepository.save(playlist);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Tạo playlist thành công!", "playlist", saved));
    }


    // Xóa playlist
    public ResponseEntity<?> deletePlaylist(Long id) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(id);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist!"));
        }

        Playlist playlist = playlistOpt.get();

        // Xóa ảnh nếu có
        if (playlist.getImage() != null) {
            String fileName = playlist.getImage().replace("/imagePlaylist/", "");
            File file = new File(new File(uploadImagePlaylistDir).getAbsolutePath(), fileName);
            if (file.exists()) file.delete();
        }

        playlistRepository.delete(playlist);

        return ResponseEntity.ok(Map.of("message", "Xóa playlist thành công!"));
    }


    // Tìm playlist theo tên trong user
    public ResponseEntity<?> searchPlaylists(Long userId, String keyword, int page, int size) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy user"));
        }

        Page<Playlist> playlists = playlistRepository.findByUserAndNameContainingIgnoreCase(
                userOpt.get(), keyword, PageRequest.of(page, size)
        );

        return ResponseEntity.ok(Map.of("playlists", playlists.getContent(),
                "total", playlists.getTotalElements()));
    }
}
