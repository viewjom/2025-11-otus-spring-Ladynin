package ru.otus.hw.repositories;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами ")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoTemplate mt;

    private static final String GENRE_ID = "1";

    @BeforeEach
    void init() {
        mt.save(new Genre("1", "Genre_1"));
        mt.save(new Genre("2", "Genre_2"));
        mt.save(new Genre("3", "Genre_3"));
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {

        Optional<Genre> actualGenre = genreRepository.findById(GENRE_ID);
        var expectedGenres = mt.findById(GENRE_ID, Genre.class);

        assertThat(actualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenres);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectBooksList() {

        var actualGenres = genreRepository.findAll();
        var expectedGenres = mt.findAll(Genre.class);;

        assertThatList(actualGenres).isEqualTo(expectedGenres);
        actualGenres.forEach(System.out::println);
    }
}