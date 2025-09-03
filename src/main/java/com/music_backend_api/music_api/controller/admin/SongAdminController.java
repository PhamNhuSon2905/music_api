package com.music_backend_api.music_api.controller.admin;

import com.music_backend_api.music_api.DTO.SongAddFormDTO;
import com.music_backend_api.music_api.DTO.SongEditFormDTO;
import com.music_backend_api.music_api.Utils.Mp3Utils;
import com.music_backend_api.music_api.model.Genre;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.repository.SongRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/songs")
public class SongAdminController {

    @Value("${upload.music-dir}")
    private String uploadMusicDir;

    @Value("${upload.image-dir}")
    private String uploadImageDir;

    private final SongRepository songRepository;

    public SongAdminController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    // Hiển thị danh sách bài hát
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "1") int pageNo,
                        @RequestParam(defaultValue = "5") int pageSize,
                        @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Song> page;

        if (keyword != null && !keyword.isEmpty()) {
            page = songRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword, pageable);
        } else {
            page = songRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        model.addAttribute("songs", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/songs/index";
    }

    // Hiển thị form thêm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("songForm", new SongAddFormDTO());
        return "admin/songs/add";
    }

    @PostMapping("/add")
    public String addSong(@Valid @ModelAttribute("songForm") SongAddFormDTO form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes,
                          Model model) {

        if (!bindingResult.hasFieldErrors("audioFile")) {
            if (!"audio/mpeg".equals(form.getAudioFile().getContentType())) {
                bindingResult.rejectValue("audioFile", "error.audioFile", "File nhạc của bài hát phải có định dạng .mp3 .Vui lòng kiểm tra lại!");
            }
        }

        if (!bindingResult.hasFieldErrors("imageFile")) {
            String imageType = form.getImageFile().getContentType();
            if (!"image/png".equals(imageType) &&
                    !"image/jpeg".equals(imageType) &&
                    !"image/webp".equals(imageType)) {
                bindingResult.rejectValue("imageFile", "error.imageFile", "Ảnh bài hát chỉ cho phép định dạng PNG, JPG, JPEG hoặc WEBP. Vui lòng kiểm tra lại!");
            }
        }


        // Nếu có lỗi thì trả về lại form
        if (bindingResult.hasErrors()) {
            return "admin/songs/add";
        }


        String musicDir = new File(uploadMusicDir).getAbsolutePath();
        String imageDir = new File(uploadImageDir).getAbsolutePath();

        try {
            String audioFileName = UUID.randomUUID().toString().substring(0, 10) + ".mp3";
            String imageFileName = UUID.randomUUID().toString().substring(0, 10) + "_" + form.getImageFile().getOriginalFilename();


            File savedAudioFile = new File(musicDir + "/" + audioFileName);
            File savedImageFile = new File(imageDir + "/" + imageFileName);

            form.getAudioFile().transferTo(savedAudioFile);
            form.getImageFile().transferTo(savedImageFile);

            int duration = Mp3Utils.getMp3DurationInSeconds(savedAudioFile);

            Song song = new Song();
            song.setId(UUID.randomUUID().toString());
            song.setTitle(form.getTitle());
            song.setAlbum(form.getAlbum());
            song.setArtist(form.getArtist());
            song.setSource("/music/" + audioFileName);
            song.setImage("/image/" + imageFileName);
            song.setDuration(duration);
            song.setFavorite(false);
            song.setCounter(0);
            song.setReplay(0);

            songRepository.save(song);
        } catch (IOException e) {
            model.addAttribute("uploadError", "Không thể lưu file nhạc hoặc ảnh. Vui lòng thử lại.");
            return "admin/songs/add";
        } catch (Exception e) {
            model.addAttribute("uploadError", "Có lỗi xảy ra khi lưu bài hát. Vui lòng thử lại.");
            return "admin/songs/add";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thêm bài hát thành công!");
        return "redirect:/admin/songs/index";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@Valid @PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài hát!");
            return "redirect:/admin/songs/index";
        }

        Song song = songOpt.get();
        SongEditFormDTO dto = new SongEditFormDTO();

        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());
        dto.setImage(song.getImage());
        dto.setSource(song.getSource());
        dto.setDuration(song.getDuration());

        model.addAttribute("songForm", dto);
        return "admin/songs/edit";
    }


    @PostMapping("/edit")
    public String editSong(@Valid @ModelAttribute("songForm") SongEditFormDTO form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        Optional<Song> existingOpt = songRepository.findById(form.getId());
        if (existingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài hát để cập nhật!");
            return "redirect:/admin/songs/index";
        }

        Song existing = existingOpt.get();

        MultipartFile audioFile = form.getAudioFile();
        MultipartFile imageFile = form.getImageFile();

        // Validate nếu người dùng upload file mới
        if (!audioFile.isEmpty() && !"audio/mpeg".equals(audioFile.getContentType())) {
            bindingResult.rejectValue("audioFile", "error.audioFile", "File nhạc phải là định dạng .mp3!");
        }

        if (!imageFile.isEmpty()) {
            String type = imageFile.getContentType();
            if (!("image/png".equals(type) || "image/jpeg".equals(type) || "image/webp".equals(type))) {
                bindingResult.rejectValue("imageFile", "error.imageFile", "Ảnh phải có định dạng PNG, JPG, JPEG hoặc WEBP!");
            }
        }

        if (bindingResult.hasErrors()) {
            form.setImage(existing.getImage());
            form.setSource(existing.getSource());
            form.setDuration(existing.getDuration());
            return "admin/songs/edit";
        }

        String musicDir = new File(uploadMusicDir).getAbsolutePath();
        String imageDir = new File(uploadImageDir).getAbsolutePath();

        try {
            String audioPath = form.getSource();
            int duration = form.getDuration();
            if (!audioFile.isEmpty()) {
                String newAudioName = UUID.randomUUID().toString().substring(0, 10) + ".mp3";
                File savedAudio = new File(musicDir + "/" + newAudioName);
                audioFile.transferTo(savedAudio);
                audioPath = "/music/" + newAudioName;
                duration = Mp3Utils.getMp3DurationInSeconds(savedAudio);
            }

            String imagePath = form.getImage();
            if (!imageFile.isEmpty()) {
                String newImageName = UUID.randomUUID().toString().substring(0, 10) + "_" + imageFile.getOriginalFilename();
                File savedImage = new File(imageDir + "/" + newImageName);
                imageFile.transferTo(savedImage);
                imagePath = "/image/" + newImageName;
            }

            existing.setTitle(form.getTitle());
            existing.setArtist(form.getArtist());
            existing.setAlbum(form.getAlbum());
            existing.setSource(audioPath);
            existing.setImage(imagePath);
            existing.setDuration(duration);

            songRepository.save(existing);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật bài hát thành công!");

            return "redirect:/admin/songs/index";

        } catch (IOException e) {
            model.addAttribute("uploadError", "Không thể lưu file. Vui lòng thử lại.");
            return "admin/songs/edit";
        }
    }


    @GetMapping("/detail/{id}")
    public String viewSongDetail(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài hát!");
            return "redirect:/admin/songs/index";
        }

        Song song = songOpt.get();
        Genre genre = song.getGenre(); // có thể null

        model.addAttribute("song", song);
        model.addAttribute("genre", genre);
        return "admin/songs/detail";
    }


    // Xử lý xoá
    @GetMapping("/delete/{id}")
    public String deleteSong(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Optional<Song> songOpt = songRepository.findById(id);
        if (songOpt.isPresent()) {
            Song song = songOpt.get();

            // Xoá file mp3
            File audioFile = new File(new File(uploadMusicDir).getAbsolutePath(), song.getSource().replace("/music/", ""));
            if (audioFile.exists()) audioFile.delete();

            // Xoá file ảnh
            File imageFile = new File(new File(uploadImageDir).getAbsolutePath(), song.getImage().replace("/image/", ""));
            if (imageFile.exists()) imageFile.delete();

            // Xoá khỏi DB
            songRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xoá bài hát thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài hát để xoá!");
        }
        return "redirect:/admin/songs/index";
    }

}