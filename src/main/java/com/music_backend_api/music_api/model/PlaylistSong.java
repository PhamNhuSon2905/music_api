package com.music_backend_api.music_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(
        name = "playlist_songs",
        uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "song_id"})
)
@Getter
@Setter
public class PlaylistSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false , fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "added_at", updatable = false, nullable = false)
    private LocalDateTime addedAt = LocalDateTime.now();

    @Column(name = "track_order" , nullable = false)
    private Integer trackOrder;

    public PlaylistSong() {

    }


    public PlaylistSong(Playlist playlist, Song song, Integer trackOrder) {
        this.playlist = playlist;
        this.song = song;
        this.trackOrder = trackOrder;
    }

}
