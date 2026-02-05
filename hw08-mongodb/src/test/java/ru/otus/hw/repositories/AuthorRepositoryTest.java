package ru.otus.hw.repositories;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;

@DisplayName("Репозиторий на основе Jpa для работы с Авторами ")
@DataMongoTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mt;

    private static final String AUTHOR_ID = "1";

    @BeforeEach
    void init() {
        authorRepository.save(new Author("1", "Author_1"));
        authorRepository.save(new Author("2", "Author_2"));
        authorRepository.save(new Author("3", "Author_3"));
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {

        Optional<Author> actualAuthor = authorRepository.findById(AUTHOR_ID);
        var expectedAuthor = mt.findById(AUTHOR_ID, Author.class);

        assertThat(actualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {

        var actualAuthor = authorRepository.findAll();
        var expectedAuthor = mt.findAll(Author.class);

        assertThatList(actualAuthor).isEqualTo(expectedAuthor);
        actualAuthor.forEach(System.out::println);
    }
}