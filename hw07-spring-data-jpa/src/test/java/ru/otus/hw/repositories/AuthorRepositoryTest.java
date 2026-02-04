package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с Авторами ")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager tem;
    private List<Author> dbAuthor;

    private static final long AUTHOR_ID = 1L;

    @BeforeEach
    void setUp() {
        dbAuthor = getDbAuthors();
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {

        Optional<Author> actualAuthor = authorRepository.findById(AUTHOR_ID);
        var expectedAuthor = tem.find(Author.class, AUTHOR_ID);

        assertThat(actualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {

        var actualAuthor = authorRepository.findAll();
        var expectedAuthor = dbAuthor;

        assertThat(actualAuthor).containsExactlyElementsOf(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }

    private List<Author> getDbAuthors() {

        return IntStream.range(1, 4).boxed()
                .map(id -> tem.find(Author.class, id))
                .toList();
    }
}