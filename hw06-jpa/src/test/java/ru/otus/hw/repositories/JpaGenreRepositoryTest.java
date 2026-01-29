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
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataJpaTest
@Import({JpaGenreRepository.class})
class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager tem;
    private List<Genre> dbGenres;

    private static final long GENRE_ID = 1L;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {

        Optional<Genre> actualGenre = jpaGenreRepository.findById(GENRE_ID);
        var expectedGenres = tem.find(Genre.class, GENRE_ID);

        assertThat(actualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectBooksList() {

        var actualGenres = jpaGenreRepository.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    private List<Genre> getDbGenres() {

        return IntStream.range(1, 4).boxed()
                .map(id -> tem.find(Genre.class, id))
                .toList();
    }
}