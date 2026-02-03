package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager tem;

    private List<Book> dbBooks;

    private static final long TEST_BOOK_ID = 1L;

    @BeforeEach
    void setUp() {
        dbBooks = getDbBooks();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBooks = getDbBooks();
        for (Book expectedBook : expectedBooks) {
            var actualBook = jpaBookRepository.findById(expectedBook.getId());

            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var expectedBooks = dbBooks;
        var actualBooks = jpaBookRepository.findAll();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0L, "Test"
                , new Author(0L, "Test")
                , new Genre(0L, "Test"));
        var actualBooks = jpaBookRepository.save(expectedBook);

        assertThat(actualBooks).isEqualTo(expectedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var book = tem.find(Book.class, TEST_BOOK_ID);
        book.setAuthor(new Author(0L, "Test"));
        book.setGenre(new Genre(0L, "Test"));
        book.setTitle("Test");
        var actualBooks = jpaBookRepository.save(book);
        var expectedBook = tem.find(Book.class, TEST_BOOK_ID);

        assertThat(actualBooks).isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        Book delBook = tem.find(Book.class, 1L);
        assertThat(delBook).isNotNull();

        jpaBookRepository.deleteById(1L);
        Book emptyBook = tem.find(Book.class, 1L);
        assertThat(emptyBook).isNull();
    }

    private List<Book> getDbBooks() {

        return IntStream.range(1, 4).boxed()
                .map(id -> tem.find(Book.class, id))
                .toList();
    }
}