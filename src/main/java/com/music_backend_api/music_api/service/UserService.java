package com.music_backend_api.music_api.service;

import com.music_backend_api.music_api.DTO.UserProfileDTO;
import com.music_backend_api.music_api.DTO.UserUpdateProfileDTO;
import com.music_backend_api.music_api.model.User;
import com.music_backend_api.music_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Value("${upload.avatar-dir}")
    private String avatarUploadDir;

    @Transactional
    public User updateUserProfile(Long userId, UserUpdateProfileDTO.UpdateUserProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Lỗi.Thông tin người dùng không tại!"));

        user.setFullname(dto.getFullname());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());

        return userRepository.save(user);
    }


    @Transactional
    public User uploadUserAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Thông tin người dùng không tồn tại!"));

        if (file.isEmpty()) {
            throw new RuntimeException("File không hợp lệ!");
        }
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (originalFilename == null || !originalFilename.toLowerCase()
                .matches(".*\\.(jpg|jpeg|png|webp|gif|bmp)$")) {
            throw new RuntimeException("File không phải là ảnh hợp lệ (tên file: " + originalFilename + ", MIME: " + contentType + ")");
        }
        try {
            String projectDir = System.getProperty("user.dir");
            Path fullPath = Paths.get(projectDir, avatarUploadDir);
            Files.createDirectories(fullPath);
            String currentAvatarPath = user.getAvatar();
            if (currentAvatarPath != null) {
                String fileName = Paths.get(currentAvatarPath).getFileName().toString();

                // Nếu không phải ảnh default thì xóa
                if (!"default_user.png".equals(fileName)) {
                    Path oldFilePath = fullPath.resolve(fileName);
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                    }
                }
            }

            String filename = user.getId() + "_" + UUID.randomUUID() + "_" + originalFilename;
            Path filePath = fullPath.resolve(filename);
            file.transferTo(filePath.toFile());

            user.setAvatar("/avatar/" + filename);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
        }

        return userRepository.save(user);
    }


}
