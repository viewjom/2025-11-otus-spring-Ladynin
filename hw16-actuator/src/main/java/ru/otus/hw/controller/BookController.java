package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final AuthorService authorService;

    private final GenreService genreService;

    //http://localhost:8080/book
    @GetMapping({"/", "/books"})
    public String findAllBooks() {
        return "bookList";
    }

    @GetMapping("/bookEdit")
    public String editBook(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "bookEdit";
    }

}