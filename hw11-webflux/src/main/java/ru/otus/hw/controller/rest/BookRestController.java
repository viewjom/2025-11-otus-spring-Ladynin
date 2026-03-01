package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveAuthorRepository;
import ru.otus.hw.repositories.ReactiveBookRepository;
import ru.otus.hw.repositories.ReactiveCommentRepository;
import ru.otus.hw.repositories.ReactiveGenreRepository;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final ReactiveBookRepository bookRepository;

    private final ReactiveCommentRepository commentRepository;

    private final ReactiveAuthorRepository authorRepository;

    private final ReactiveGenreRepository genreRepository;

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

    @PostMapping("/api/books/0")
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody BookDto book) {
        return authorRepository.findById(book.getAuthorDto().getId())
                .flatMap(author -> genreRepository.findById(book.getGenreDto().getId())
                        .flatMap(genre -> {
                                    if (book.getId().equals("0")) {
                                        book.setId(null);
                                    }
                                    return bookRepository.save(bookDtoConverter.toModel(book, author, genre));
                                }
                        ))
                .map(newBook -> new ResponseEntity<>(bookDtoConverter.getDto(newBook), HttpStatus.OK))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDto>> updateBook(@RequestBody BookDto book, @PathVariable(value = "id") String id) {
        Mono<Author> authorMono = authorRepository.findById(book.getAuthorDto().getId());
        Mono<Genre> genreMono = genreRepository.findById(book.getGenreDto().getId());
        return Mono.zip(authorMono, genreMono)
                .flatMap(p ->
                    bookRepository.save(bookDtoConverter.toModel(book, p.getT1(), p.getT2())))
                .map(newBook -> new ResponseEntity<>(bookDtoConverter.getDto(newBook), HttpStatus.OK))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return commentRepository.deleteAllByBookId(id).then(bookRepository.deleteById(id))
                .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.OK)));
    }
}