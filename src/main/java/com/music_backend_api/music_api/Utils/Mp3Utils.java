package com.music_backend_api.music_api.Utils;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;

public class Mp3Utils {

    public static int getMp3DurationInSeconds(File mp3File) {
        try {
            Mp3File mp3 = new Mp3File(mp3File);
            return (int) mp3.getLengthInSeconds(); // Thời lượng tính bằng giây
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
