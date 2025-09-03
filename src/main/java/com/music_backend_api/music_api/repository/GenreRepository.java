package com.music_backend_api.music_api.repository;

import com.music_backend_api.music_api.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<Genre, Long> {

    Page<Genre> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
