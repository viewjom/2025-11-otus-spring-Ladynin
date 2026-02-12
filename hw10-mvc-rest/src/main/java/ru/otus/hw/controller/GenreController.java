package ru.otus.hw.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class GenreController {

    private final GenreService genreService;

    //http://localhost:8080/genre
    @GetMapping({"/genre", "/genres"})
    public String findAllGenres(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genreList";
    }
}