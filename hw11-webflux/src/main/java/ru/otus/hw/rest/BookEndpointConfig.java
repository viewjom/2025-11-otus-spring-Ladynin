package ru.otus.hw.rest;

import java.util.Comparator;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import ru.otus.hw.repositories.ReactiveBookRepository;
import ru.otus.hw.repositories.ReactiveCommentRepository;

@Configuration
@RequiredArgsConstructor
public class BookEndpointConfig {

    private final ReactiveBookRepository bookRepository;

    private final ReactiveCommentRepository commentRepository;

    private final BookDtoConverter bookConverter;

    @Bean
    public RouterFunction<ServerResponse> bookRouterFunction() {
        return route()
                .GET("/api/books/{id}", this::getById)
                .GET("/api/books", this::getAll)
                .POST("/api/books", this::saveBook)
                .DELETE("/api/books/{id}", this::deleteBook)
                .build();
    }

    private Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<BookDto> books = bookRepository.findAll()
                .map(bookConverter::getDto)
                .sort(Comparator.comparing(BookDto::getTitle));
        return ok().body(books, BookDto.class);
    }

    private Mono<ServerResponse> getById(ServerRequest request) {
        Mono<BookDto> book = bookRepository.findById(request.pathVariable("id"))
                .map(bookConverter::getDto);
        return ok().body(book, BookDto.class);
    }

    private Mono<ServerResponse> saveBook(ServerRequest request) {
        return request.bodyToMono(BookDto.class)
                .flatMap(book -> {
                    Author author = new Author(book.getAuthorDto().getId(), book.getAuthorDto().getFullName());
                    Genre genre = new Genre(book.getGenreDto().getId(), book.getGenreDto().getName());
                        String bookId = !book.getId().equals("") ? book.getId() : null;
                        return bookRepository.save(new Book(bookId, book.getTitle(), author, genre));
                }).map(bookConverter::getDto)
                .flatMap(book -> ok().body(fromValue(book)));
    }

    private Mono<ServerResponse> deleteBook(ServerRequest request) {
        commentRepository.deleteAllByBookId(request.pathVariable("id"));
        return bookRepository.findById(request.pathVariable("id"))
                .flatMap(bookRepository::delete)
                .then(ok().build());
    }
}