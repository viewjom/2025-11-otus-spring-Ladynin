package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveBookRepository;
import ru.otus.hw.repositories.ReactiveCommentRepository;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final ReactiveBookRepository bookRepository;

    private final ReactiveCommentRepository commentRepository;

    private final BookDtoConverter bookDtoConverter;

    @GetMapping("/api/books")
    public Flux<BookDto> allBooksList() {
        return bookRepository.findAll()
                .map(bookDtoConverter::getDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable(value = "id", required = false) String id) {
        return bookRepository.findById(id)
                .map(bookDtoConverter::getDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody BookDto book) {
        Author author = new Author(book.getAuthorDto().getId(), book.getAuthorDto().getFullName());
        Genre genre = new Genre(book.getGenreDto().getId(), book.getGenreDto().getName());
        String bookId = !book.getId().equals("") ? book.getId() : null;
        return bookRepository.save(new Book(bookId, book.getTitle(), author, genre))
                .map(b -> new ResponseEntity<>(bookDtoConverter.getDto(b), HttpStatus.CREATED))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));

    }

    @DeleteMapping("/api/books/{id}")
   // @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        commentRepository.deleteAllByBookId(id);
        return bookRepository.deleteById(id)
                .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.OK)));
    }
}