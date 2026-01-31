package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.List;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

@DisplayName("Сервис для работы с книгами должен")
@DataJpaTest
@Import({BookServiceImpl.class, JpaGenreRepository.class, JpaAuthorRepository.class, JpaBookRepository.class,
        BookConverter.class, AuthorConverter.class, GenreConverter.class})
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Autowired
    BookConverter bookConverter;

    private static final long TEST_BOOK_ID = 1L;

    private static final String TEST_TITLE = "TEST_TITLE";


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        Book book = assertDoesNotThrow(() -> {
            return bookServiceImpl.findById(TEST_BOOK_ID).get();
        });
        assertThat(book.getId()).isEqualTo(TEST_BOOK_ID);
        System.out.println(bookConverter.bookToString(book));
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        List<Book> books = assertDoesNotThrow(() -> {
            return bookServiceImpl.findAll();
        });
        books.forEach(b -> System.out.println(bookConverter.bookToString(b)));
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book book = assertDoesNotThrow(() -> {
            return bookServiceImpl.insert(TEST_TITLE, 1L, 1L);
        });
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE);
        System.out.println(bookConverter.bookToString(book));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Book book = assertDoesNotThrow(() -> {
            return bookServiceImpl.update(TEST_BOOK_ID, TEST_TITLE, 2L, 2L);
        });
        assertThat(book.getTitle()).isEqualTo(TEST_TITLE);
        System.out.println(bookConverter.bookToString(book));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertDoesNotThrow(() -> {
            bookServiceImpl.deleteById(TEST_BOOK_ID);
        });
    }
}