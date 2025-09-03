package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.model.Genre;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.repository.GenreRepository;
import com.music_backend_api.music_api.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private SongRepository songRepository;

    public List<Map<String, Object>> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Genre genre : genres) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", genre.getId());
            map.put("name", genre.getName());
            map.put("description", genre.getDescription());
            result.add(map);
        }

        return result;
    }

    public List<Map<String, Object>> getSongsByGenre(Long genreId) {
        List<Song> songs = songRepository.findByGenreId(genreId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Song song : songs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", song.getId());
            map.put("title", song.getTitle());
            map.put("artist", song.getArtist());
            map.put("album", song.getAlbum());
            map.put("image", song.getImage());
            map.put("source", song.getSource());
            map.put("duration", song.getDuration());
            map.put("favorite", song.isFavorite());
            map.put("counter", song.getCounter());
            map.put("replay", song.getReplay());
            map.put("createdAt", song.getCreatedAt());
            map.put("genre", song.getGenre() != null ? song.getGenre().getName() : null);

            result.add(map);
        }

        return result;
    }
}
