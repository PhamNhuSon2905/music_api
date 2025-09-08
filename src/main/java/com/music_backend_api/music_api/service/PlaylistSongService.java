package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.RequestUserDTO.PlaylistSongRequest;
import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.model.PlaylistSong;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.repository.PlaylistRepository;
import com.music_backend_api.music_api.repository.PlaylistSongRepository;
import com.music_backend_api.music_api.repository.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PlaylistSongService {

    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public PlaylistSongService(
            PlaylistSongRepository playlistSongRepository,
            PlaylistRepository playlistRepository,
            SongRepository songRepository
    ) {
        this.playlistSongRepository = playlistSongRepository;
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    // Lấy danh sách bài hát trong playlist
    public ResponseEntity<?> getSongsInPlaylist(Long playlistId, int page, int size) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }

        Page<PlaylistSong> songs = playlistSongRepository.findByPlaylistOrderByTrackOrderAsc(
                playlistOpt.get(), PageRequest.of(page, size));

        return ResponseEntity.ok(Map.of(
                "songs", songs.getContent(),
                "total", songs.getTotalElements()
        ));
    }

    // Thêm bài hát vào playlist
    public ResponseEntity<?> addSongToPlaylist(PlaylistSongRequest request) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(request.getPlaylistId());
        Optional<Song> songOpt = songRepository.findById(request.getSongId()); // songId đã là String

        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }
        if (songOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy bài hát"));
        }
        PlaylistSong playlistSong = new PlaylistSong(
                playlistOpt.get(),
                songOpt.get(),
                request.getTrackOrder() != null ? request.getTrackOrder() : 0
        );

        PlaylistSong saved = playlistSongRepository.save(playlistSong);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Thêm bài hát vào playlist thành công", "playlistSong", saved));
    }

    // Xóa bài hát khỏi playlist
    public ResponseEntity<?> removeSongFromPlaylist(Long playlistId, String songId) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        Optional<Song> songOpt = songRepository.findById(songId); // songId là String

        if (playlistOpt.isEmpty() || songOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist hoặc bài hát"));
        }

        playlistSongRepository.deleteByPlaylistAndSong(playlistOpt.get(), songOpt.get());

        return ResponseEntity.ok(Map.of("message", "Xóa bài hát khỏi playlist thành công"));
    }

    // Tìm kiếm bài hát trong playlist theo tên
    public ResponseEntity<?> searchSongsInPlaylist(Long playlistId, String keyword, int page, int size) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }

        Page<PlaylistSong> songs = playlistSongRepository
                .findByPlaylistAndSong_TitleContainingIgnoreCaseOrderByTrackOrderAsc(
                        playlistOpt.get(), keyword, PageRequest.of(page, size)
                );

        return ResponseEntity.ok(Map.of(
                "songs", songs.getContent(),
                "total", songs.getTotalElements()
        ));
    }
}
