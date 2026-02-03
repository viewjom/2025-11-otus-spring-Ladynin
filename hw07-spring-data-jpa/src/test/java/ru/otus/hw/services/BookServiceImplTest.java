package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.Book;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.List;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@DisplayName("Сервис для работы с книгами должен")
@DataJpaTest
@Import({BookServiceImpl.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private BookConverter bookConverter;

    private static final long TEST_BOOK_ID = 1L;

    private static final String TEST_TITLE = "TEST_TITLE";


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        assertDoesNotThrow(() -> {
            Book book = bookServiceImpl.findById(TEST_BOOK_ID).get();
            System.out.println(bookConverter.bookToString(book));
        });
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        assertDoesNotThrow(() -> {
            List<Book> books =  bookServiceImpl.findAll();
            books.forEach(b -> System.out.println(bookConverter.bookToString(b)));
        });
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        assertDoesNotThrow(() -> {
            Book book =  bookServiceImpl.insert(TEST_TITLE, 1L, 1L);
            System.out.println(bookConverter.bookToString(book));
        });
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        assertDoesNotThrow(() -> {
            Book book = bookServiceImpl.update(TEST_BOOK_ID, TEST_TITLE, 2L, 2L);
            System.out.println(bookConverter.bookToString(book));
        });
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertDoesNotThrow(() -> {
            bookServiceImpl.deleteById(3L);
        });
    }
}