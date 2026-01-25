package ru.otus.hw.repositories;

import java.util.Optional;
import org.assertj.core.api.OptionalAssert;
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
@Import({JpaBookRepository.class, JpaAuthorRepository.class, JpaGenreRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager tem;

    private List<Book> dbBooks;

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
        var actualBooks = jpaBookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Author author = jpaAuthorRepository.findById(1L).get();
        Genre genre = jpaGenreRepository.findById(1L).get();
        var newBook = new Book(0, "BookTitle_10500",
                author,
                genre);

        var returnedBook = jpaBookRepository.save(newBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);

        assertThat(jpaBookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        Author author = jpaAuthorRepository.findById(1L).get();
        Genre genre = jpaGenreRepository.findById(1L).get();
        var expectedBook = new Book(1L, "BookTitle_10500", author, genre);

        assertThat(jpaBookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = jpaBookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(jpaBookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {

        //Book delBook = tem.find(Book.class, 1L);
        Book delBook = jpaBookRepository.findById(1L).get();

        assertThat(delBook).isNotNull();
        jpaBookRepository.deleteById(1L);
        Optional<Book> emptyBook = jpaBookRepository.findById(1L);
        assertThat(emptyBook).isEmpty();
    }

    private List<Book> getDbBooks() {

        return IntStream.range(1, 4).boxed()
                .map(id -> tem.find(Book.class, id))
                .toList();
    }
}