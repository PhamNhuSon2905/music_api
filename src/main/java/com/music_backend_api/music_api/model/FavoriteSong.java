package com.music_backend_api.music_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "favorite_songs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "song_id"})
)
@Getter
@Setter
public class FavoriteSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(nullable = true)
    private Boolean favorite = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Optional: Constructors
    public FavoriteSong() {
    }

    public FavoriteSong(User user, Song song, Boolean favorite) {
        this.user = user;
        this.song = song;
        this.favorite = favorite;
    }
}
