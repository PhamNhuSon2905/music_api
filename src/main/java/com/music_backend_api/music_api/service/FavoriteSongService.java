package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.DTO.UserFavoriteSongDTO;
import com.music_backend_api.music_api.model.FavoriteSong;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.FavoriteSongRepository;
import com.music_backend_api.music_api.repository.SongRepository;
import com.music_backend_api.music_api.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteSongService {

    @Autowired
    private FavoriteSongRepository favoriteSongRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    public void addFavoriteSong(Long userId, String songId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Song song = songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));

        favoriteSongRepository.findByUserAndSong(user, song)
                .orElseGet(() -> {
                    FavoriteSong fav = new FavoriteSong(user, song, true);
                    return favoriteSongRepository.save(fav);
                });
    }
    @Transactional
    public void removeFavoriteSong(Long userId, String songId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Song song = songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));

        favoriteSongRepository.deleteByUserAndSong(user, song);
    }

    public List<UserFavoriteSongDTO> getFavoriteSongsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteSongRepository.findByUser(user)
                .stream()
                .map(UserFavoriteSongDTO::new)  // <-- truyền FavoriteSong
                .collect(Collectors.toList());
    }
}
