package com.music_backend_api.music_api.repository;
import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    // Lấy tất cả playlist của 1 user với phân trang
    Page<Playlist> findByUser(User user, Pageable pageable);

    // Tìm kiếm playlist theo tên với phân trang
    Page<Playlist> findByUserAndNameContainingIgnoreCase(User user, String name, Pageable pageable);

}
