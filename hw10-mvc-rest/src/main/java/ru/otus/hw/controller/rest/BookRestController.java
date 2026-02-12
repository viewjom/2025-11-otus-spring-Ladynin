package ru.otus.hw.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/api/books")
    public List<BookDto> getAll() {
        return bookService.findAll();
    }

    @PostMapping("/api/book")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookDto bookDto) {
        var savedBookDto = bookService.update(bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthorDto().getId(),
                bookDto.getGenreDto().getId());
        return ResponseEntity.ok(savedBookDto);
    }

    @DeleteMapping("/api/book/{id}")
    public void delete(@PathVariable long id) {
        bookService.deleteById(id);
    }
}