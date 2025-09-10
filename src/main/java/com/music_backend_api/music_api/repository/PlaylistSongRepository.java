package com.music_backend_api.music_api.repository;

import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.model.PlaylistSong;
import com.music_backend_api.music_api.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    // Lấy tất cả bài hát trong playlist, sắp xếp theo thời gian thêm mới nhất
    Page<PlaylistSong> findByPlaylistOrderByAddedAtDesc(Playlist playlist, Pageable pageable);

    // Kiểm tra bài hát có trong playlist không
    Optional<PlaylistSong> findByPlaylistAndSong(Playlist playlist, Song song);

    // Xóa bài hát khỏi playlist
    void deleteByPlaylistAndSong(Playlist playlist, Song song);

    // Tìm kiếm bài hát theo tên trong playlist, sắp xếp theo thời gian thêm mới nhất
    Page<PlaylistSong> findByPlaylistAndSong_TitleContainingIgnoreCaseOrderByAddedAtDesc(
            Playlist playlist, String keyword, Pageable pageable
    );
}
