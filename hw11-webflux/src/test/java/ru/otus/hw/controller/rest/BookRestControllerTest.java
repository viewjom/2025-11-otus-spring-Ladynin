package ru.otus.hw.controller.rest;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveAuthorRepository;
import ru.otus.hw.repositories.ReactiveBookRepository;
import ru.otus.hw.repositories.ReactiveCommentRepository;
import ru.otus.hw.repositories.ReactiveGenreRepository;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({BookDtoConverter.class, CommentDtoConverter.class, AuthorDtoConverter.class, GenreDtoConverter.class})
@WebFluxTest(controllers = BookRestController.class)
public class BookRestControllerTest {

    private static final String TEST_ID = "1";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookDtoConverter bookDtoConverter;

    @MockBean
    private ReactiveBookRepository bookRepository;

    @MockBean
    private ReactiveCommentRepository commentRepository;

    @MockBean
    private ReactiveAuthorRepository authorRepository;

    @MockBean
    private ReactiveGenreRepository genreRepository;

    @DisplayName("Должен вернуть все книги")
    @Test
    void shouldGetAllBook() {
        var expectedBookList = getTestListBook();

        List<BookDto> expectedDtoResult = expectedBookList.stream()
                .map(val -> bookDtoConverter.getDto(val)).toList();

        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(expectedBookList));

        var result = webTestClient
                .get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThatList(result).isEqualTo(expectedDtoResult);
    }

    @DisplayName("Должен вернуть книгу по id")
    @Test
    void shouldGetBookById() {
        Book expectedBook = getTestListBook().get(0);

        BookDto expectedDtoResult = bookDtoConverter.getDto(expectedBook);

        when(bookRepository.findById(TEST_ID)).thenReturn(Mono.just(expectedBook));

        var result = webTestClient
                .get().uri("/api/books/" + TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThat(result).isEqualTo(List.of(expectedDtoResult));
    }

    @DisplayName("Должен изменить книгу")
    @Test
    void shouldSaveBook() {
        Book expectedBook = getTestListBook().get(0);
        expectedBook.setTitle("NewTitle");
        Author expectedAuthor = new Author("2", "Author_2");
        Genre expectedGenre = new Genre("2", "Genre_2");
        expectedBook.setAuthor(expectedAuthor);
        expectedBook.setGenre(expectedGenre);
        BookDto expectedDtoResult = bookDtoConverter.getDto(expectedBook);


        when(authorRepository.findById("2")).thenReturn(Mono.just(expectedAuthor));
        when(genreRepository.findById("2")).thenReturn(Mono.just(expectedGenre));
        when(bookRepository.findById(TEST_ID)).thenReturn(Mono.just(expectedBook));
        when(bookRepository.save(expectedBook)).thenReturn(Mono.just(expectedBook));

        var result = webTestClient
                .post().uri("/api/books/" + TEST_ID)
                .bodyValue(expectedDtoResult)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThat(result).isEqualTo(List.of(expectedDtoResult));
    }

    @DisplayName("Должен создать книгу")
    @Test
    void shouldCreateBook() {
        Author expectedAuthor = new Author(TEST_ID, "Author_1");
        Genre expectedGenre = new Genre(TEST_ID, "Genre_1");
        Book expectedNewBook = new Book("1000","Test", expectedAuthor, expectedGenre);
        BookDto expectedBookDto = bookDtoConverter.getDto(expectedNewBook);

        when(authorRepository.findById(TEST_ID)).thenReturn(Mono.just(expectedAuthor));
        when(genreRepository.findById(TEST_ID)).thenReturn(Mono.just(expectedGenre));
        when(bookRepository.save(expectedNewBook)).thenReturn(Mono.just(expectedNewBook));

        var result = webTestClient
                .post().uri("/api/books/0")
                .bodyValue(expectedBookDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThat(result).isEqualTo(List.of(expectedBookDto));
    }

    @DisplayName("Должен удалить книгу")
    @Test
    void shouldDelBook() {
        when(commentRepository.deleteAllByBookId((String) any())).thenReturn(Mono.empty());
        when(bookRepository.deleteById((String) any())).thenReturn(Mono.empty());

        webTestClient
                .delete().uri("/api/books/" + TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseBody();
    }


    private List<Book> getTestListBook() {
        Book book1 = new Book(TEST_ID, "BookTitle_1",
                new Author(TEST_ID, "Author_1"),
                new Genre(TEST_ID, "Genre_1"));
        new Comment(TEST_ID, "Хорошая книга", book1);
        new Comment("2", "Очень хорошая книга", book1);
        Book book2 = new Book("2", "BookTitle_2",
                new Author("2", "Author_2"),
                new Genre("2", "Genre_2"));
        new Comment("3", "Нормальная книга", book2);
        new Comment("4", "Прекрасная книга", book2);
        Book book3 = new Book("3", "BookTitle_2",
                new Author("3", "Author_2"),
                new Genre("3", "Genre_2"));
        new Comment("5", "Великолепная книга", book3);

        return Arrays.asList(book1, book2, book3);
    }
}

