package com.music_backend_api.music_api.repository;

import com.music_backend_api.music_api.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, String> {
    // Phân trang + lọc theo tiêu đề
    Page<Song> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    // Toàn bộ bài hát có phân trang, mới nhất trước createdAt mới nhất
    Page<Song> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.album) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> searchByKeyword(@Param("keyword") String keyword);


    // Lấy danh sách bài hát theo thể loại
    List<Song> findByGenreId(Long genreId);

    List<Song> findByGenreIsNull();

    List<Song> findAllByIdIn(List<String> ids);
}
