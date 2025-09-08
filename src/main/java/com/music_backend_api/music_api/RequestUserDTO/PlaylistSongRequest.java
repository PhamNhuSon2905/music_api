package com.music_backend_api.music_api.RequestUserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistSongRequest {

    private Long playlistId;
    private String songId;
    private Integer trackOrder;
}
