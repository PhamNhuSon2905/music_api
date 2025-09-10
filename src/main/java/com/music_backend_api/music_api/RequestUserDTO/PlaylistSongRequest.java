package com.music_backend_api.music_api.RequestUserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistSongRequest {

    @NotNull(message = "PlaylistId không được để trống!")
    private Long playlistId;

    @NotNull(message = "SongId không được để trống!")
    private String songId;

}
