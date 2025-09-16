package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.DTO.SongAddPlaylistDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Lấy danh sách bài hát trong playlist (mới thêm sẽ lên đầu)
    public ResponseEntity<?> getSongsInPlaylist(Long playlistId, int page, int size) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }

        Page<PlaylistSong> songsPage = playlistSongRepository.findByPlaylistOrderByAddedAtDesc(
                playlistOpt.get(), PageRequest.of(page, size));

        List<SongAddPlaylistDTO> dtoList = songsPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "songs", dtoList,
                "total", songsPage.getTotalElements()
        ));
    }

    // Thêm bài hát vào playlist (mới thêm sẽ lên đầu nhờ addedAt)
    public ResponseEntity<?> addSongToPlaylist(PlaylistSongRequest request) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(request.getPlaylistId());
        Optional<Song> songOpt = songRepository.findById(request.getSongId());

        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }
        if (songOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy bài hát"));
        }

        Playlist playlist = playlistOpt.get();
        Song song = songOpt.get();

        // Check trùng
        if (playlistSongRepository.findByPlaylistAndSong(playlist, song).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Bài hát đã tồn tại trong playlist"));
        }

        PlaylistSong playlistSong = new PlaylistSong(playlist, song);
        PlaylistSong saved = playlistSongRepository.save(playlistSong);

        SongAddPlaylistDTO dto = toDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Thêm bài hát vào playlist thành công", "song", dto));
    }

    // Xóa bài hát khỏi playlist
    @Transactional
    public ResponseEntity<?> removeSongFromPlaylist(Long playlistId, String songId) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        Optional<Song> songOpt = songRepository.findById(songId);

        if (playlistOpt.isEmpty() || songOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist hoặc bài hát"));
        }

        playlistSongRepository.deleteByPlaylistAndSong(playlistOpt.get(), songOpt.get());

        return ResponseEntity.ok(Map.of("message", "Xóa bài hát khỏi playlist thành công"));
    }

    // Tìm kiếm bài hát trong playlist
    public ResponseEntity<?> searchSongsInPlaylist(Long playlistId, String keyword, int page, int size) {
        Optional<Playlist> playlistOpt = playlistRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Không tìm thấy playlist"));
        }

        Page<PlaylistSong> songsPage = playlistSongRepository
                .findByPlaylistAndSong_TitleContainingIgnoreCaseOrderByAddedAtDesc(
                        playlistOpt.get(), keyword, PageRequest.of(page, size)
                );

        List<SongAddPlaylistDTO> dtoList = songsPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "songs", dtoList,
                "total", songsPage.getTotalElements()
        ));
    }

    // Helper Convert entity -> DTO
    private SongAddPlaylistDTO toDTO(PlaylistSong ps) {
        Song song = ps.getSong();
        SongAddPlaylistDTO dto = new SongAddPlaylistDTO();
        dto.setSongId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setAlbum(song.getAlbum());
        dto.setArtist(song.getArtist());
        dto.setSource(song.getSource());
        dto.setImage(song.getImage());
        dto.setDuration(song.getDuration());
        dto.setCreatedAt(song.getCreatedAt());
        dto.setAdded(true);
        dto.setAddedAt(ps.getAddedAt());
        return dto;
    }
}
