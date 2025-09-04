package com.music_backend_api.music_api.repository;
import com.music_backend_api.music_api.model.Playlist;
import com.music_backend_api.music_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    // Lấy tất cả playlist của 1 user
    List<Playlist> findPlaylistByUser(User user);

    // Tìm kiếm playlist theo tên (có thể dùng contains để search linh hoạt)
    List<Playlist> findPlaylistByName(User user, String name);

}
