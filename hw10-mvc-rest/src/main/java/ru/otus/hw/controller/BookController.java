package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    //http://localhost:8080/book
    @GetMapping({"/", "book", "/books"})
    public String findAllBooks() {
        return "bookList";
    }

    @GetMapping("/bookEdit")
    public String editBook(@RequestParam(value = "id", required = false) Long id,
                           Model model) {
        if (id == null) {
            model.addAttribute("book", BookDto.BOOK_DTO_EMPTY);
        } else {
            BookDto bookDto = bookService.findById(id);
            model.addAttribute("book", bookDto);
        }
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "bookEdit";
    }
}