package com.music_backend_api.music_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "songs")
public class Song {

    @Setter
    @Id
    @Column(name = "id")
    private String id;
    @Setter
    private String title;
    @Setter
    private String album;
    @Setter
    private String artist;
    @Setter
    private String source;
    @Setter
    private String image;

    @Setter
    private int duration;
    @Setter
    private boolean favorite;
    @Setter
    private int counter;
    @Setter
    private int replay;


    @Column(name = "created_at", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


    public Song() {
    }

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @Setter
    private Genre genre;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteSong> likedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

}
