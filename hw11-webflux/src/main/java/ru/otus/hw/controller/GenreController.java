package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class GenreController {

    //http://localhost:8080/genre
    @GetMapping({"/genres"})
    public String findAllGenres() {
        return "genreList";
    }
}