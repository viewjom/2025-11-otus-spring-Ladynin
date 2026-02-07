package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.helper.Generator;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mt;

    private static final String TEST_BOOK_ID = "1";

    @BeforeEach
    void init() {
        Generator.initBooks(mt);
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBooks = mt.findAll(Book.class);
        for (Book expectedBook : expectedBooks) {
            var actualBook = bookRepository.findById(expectedBook.getId());

            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var expectedBooks = mt.findAll(Book.class);
        var actualBooks = bookRepository.findAll();

        assertThatList(actualBooks).isEqualTo(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "Test"
                , new Author(null, "Test")
                , new Genre(null, "Test"));
        var actualBooks = bookRepository.save(expectedBook);

        assertThat(actualBooks).isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var book = mt.findById(TEST_BOOK_ID, Book.class);
        book.setAuthor(new Author(null, "Test"));
        book.setGenre(new Genre(null, "Test"));
        book.setTitle("Test");
        var actualBooks = bookRepository.save(book);
        var expectedBook = mt.findById(TEST_BOOK_ID, Book.class);

        assertThat(actualBooks).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Book delBook = mt.findById("1", Book.class);
        assertThat(delBook).isNotNull();

        bookRepository.deleteById("1");
        Book emptyBook = mt.findById("1", Book.class);
        assertThat(emptyBook).isNull();
    }
}