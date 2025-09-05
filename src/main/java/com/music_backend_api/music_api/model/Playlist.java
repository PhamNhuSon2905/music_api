package com.music_backend_api.music_api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
@Getter
@Setter
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "created_at", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.LAZY)
    private List<PlaylistSong> playListSongs = new ArrayList<>();

}
