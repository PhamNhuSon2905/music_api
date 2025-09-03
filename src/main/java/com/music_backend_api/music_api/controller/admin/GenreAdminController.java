package com.music_backend_api.music_api.controller.admin;

import com.music_backend_api.music_api.DTO.GenreAddFormDTO;
import com.music_backend_api.music_api.DTO.GenreAssignSongDTO;
import com.music_backend_api.music_api.DTO.GenreEditFormDTO;
import com.music_backend_api.music_api.model.Genre;
import com.music_backend_api.music_api.model.Song;
import com.music_backend_api.music_api.repository.GenreRepository;
import com.music_backend_api.music_api.repository.SongRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/genres")
public class GenreAdminController {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private SongRepository songRepository;

    // Hiển thị danh sách thể loại
    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(defaultValue = "1") int pageNo,
                        @RequestParam(defaultValue = "5") int pageSize,
                        @RequestParam(defaultValue = "") String keyword) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Genre> page = keyword.isEmpty()
                ? genreRepository.findAll(pageable)
                : genreRepository.findByNameContainingIgnoreCase(keyword, pageable);

        model.addAttribute("genres", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/genres/index";
    }

    // Hiển thị form thêm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("genreForm", new GenreAddFormDTO());
        return "admin/genres/add";
    }

    // Xử lý thêm thể loại
    @PostMapping("/add")
    public String addGenre(@Valid @ModelAttribute("genreForm") GenreAddFormDTO genreForm,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/genres/add";
        }

        Genre genre = new Genre();
        genre.setName(genreForm.getName());
        genre.setDescription(genreForm.getDescription());

        genreRepository.save(genre);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thể loại thành công!");
        return "redirect:/admin/genres/index";
    }


    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Genre> genreOpt = genreRepository.findById(id);
        if (genreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại!");
            return "redirect:/admin/genres/index";
        }

        Genre genre = genreOpt.get();
        GenreEditFormDTO formDTO = new GenreEditFormDTO();
        formDTO.setId(genre.getId());
        formDTO.setName(genre.getName());
        formDTO.setDescription(genre.getDescription());

        model.addAttribute("genreForm", formDTO);
        return "admin/genres/edit";
    }

    // Xử lý cập nhật
    @PostMapping("/edit")
    public String editGenre(@Valid @ModelAttribute("genreForm") GenreEditFormDTO form,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/genres/edit";
        }

        Optional<Genre> genreOpt = genreRepository.findById(form.getId());
        if (genreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại!");
            return "redirect:/admin/genres/index";
        }

        Genre genre = genreOpt.get();
        genre.setName(form.getName());
        genre.setDescription(form.getDescription());

        genreRepository.save(genre);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thể loại thành công!");
        return "redirect:/admin/genres/index";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Genre> genreOpt = genreRepository.findById(id);
        if (genreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại!");
            return "redirect:/admin/genres/index";
        }

        Genre genre = genreOpt.get();
        List<Song> songs = songRepository.findByGenreId(id);  // Lấy bài hát theo id thể loại

        model.addAttribute("genre", genre);
        model.addAttribute("songs", songs);

        return "admin/genres/detail";
    }


    @GetMapping("/delete/{id}")
    public String deleteGenre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Genre> genreOpt = genreRepository.findById(id);
        if (genreOpt.isPresent()) {
            // Kiểm tra xem có bài hát nào thuộc thể loại này không
            List<Song> songs = songRepository.findByGenreId(id);
            if (!songs.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không thể xoá! Thể loại này đang chứa các bài hát.");
                return "redirect:/admin/genres/index";
            }

            // Nếu không có bài hát, cho xoá
            genreRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xoá thể loại thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại để xoá!");
        }

        return "redirect:/admin/genres/index";
    }


    // hiển thị các bài hát có trong thể loại
    @GetMapping("/assign-songs/{id}")
    public String showAssignSongsForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Genre> genreOpt = genreRepository.findById(id);
        if (genreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại!");
            return "redirect:/admin/genres/index";
        }

        List<Song> unassignedSongs = songRepository.findByGenreIsNull();

        model.addAttribute("genre", genreOpt.get());
        model.addAttribute("unassignedSongs", unassignedSongs);
        model.addAttribute("assignForm", new GenreAssignSongDTO());
        return "admin/genres/assign_songs";
    }

    // Thêm các bài hát vào 1 thể loại
    @PostMapping("/assign-songs")
    public String assignSongsToGenre(@ModelAttribute GenreAssignSongDTO form,
                                     RedirectAttributes redirectAttributes) {
        Optional<Genre> genreOpt = genreRepository.findById(form.getGenreId());
        if (genreOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thể loại!");
            return "redirect:/admin/genres/index";
        }

        Genre genre = genreOpt.get();
        for (String songId : form.getSongIds()) {
            songRepository.findById(songId).ifPresent(song -> {
                song.setGenre(genre);
                songRepository.save(song);
            });
        }

        redirectAttributes.addFlashAttribute("successMessage", "Gán bài hát thành công!");
        return "redirect:/admin/genres/detail/" + form.getGenreId();
    }

    // gỡ bài hát khỏi thể loại
    @GetMapping("/unassign-song")
    public String unassignSongFromGenre(@RequestParam("genreId") Long genreId,
                                        @RequestParam("songId") String songId,
                                        RedirectAttributes redirectAttributes) {
        Optional<Song> songOpt = songRepository.findById(songId);
        if (songOpt.isPresent()) {
            Song song = songOpt.get();
            song.setGenre(null);
            songRepository.save(song);
            redirectAttributes.addFlashAttribute("successMessage", "Đã gỡ bài hát khỏi thể loại!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bài hát để gỡ!");
        }

        return "redirect:/admin/genres/detail/" + genreId;
    }


}
