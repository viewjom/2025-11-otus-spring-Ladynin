package ru.otus.hw.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.converters.BookConverter;
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

    private final BookConverter bookConverter;


    @GetMapping({"/", "/book", "/books"})
    public String findAllBooks(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/bookEdit")
    public String editBook(@RequestParam(value = "id", required = false) Long id,
                           Model model) {

        if (id == null) {
            model.addAttribute("book", bookConverter.getDto());
        } else {
            BookDto bookDto = bookService.findById(id);
            model.addAttribute("book", bookDto);
        }
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "bookEdit";
    }

    @PostMapping("/bookEdit")
    public String saveBook(@ModelAttribute("book") BookDto book) {
        bookService.update(book.getId(),
                book.getTitle(),
                book.getAuthorDto().getId(),
                book.getGenreDto().getId());
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}