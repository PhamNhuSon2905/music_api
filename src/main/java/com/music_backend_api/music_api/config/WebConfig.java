package com.music_backend_api.music_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map đường dẫn /imagePlaylist/** tới thư mục uploads/imagePlaylist/
        registry.addResourceHandler("/imagePlaylist/**")
                .addResourceLocations("file:uploads/imagePlaylist/");

        // Nếu muốn map thêm avatar, image, music thì có thể gom hết ở đây
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:uploads/avatar/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:uploads/image/");

        registry.addResourceHandler("/music/**")
                .addResourceLocations("file:uploads/music/");
    }
}
