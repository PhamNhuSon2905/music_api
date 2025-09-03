package com.music_backend_api.music_api.repository;

import com.music_backend_api.music_api.model.FavoriteSong;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {
    Optional<FavoriteSong> findByUserAndSong(User user, Song song);

    List<FavoriteSong> findByUser(User user);

    void deleteByUserAndSong(User user, Song song);
}
