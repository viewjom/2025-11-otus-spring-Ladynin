package ru.otus.hw.services;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class,
        CommentDtoConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    private static final long TEST_BOOK_ID = 1L;

    private static final long TEST_COMMENT_ID = 3L;

    private static final String TEST_COMMENT = "TEST_COMMENT";

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        assertDoesNotThrow(() -> {
            CommentDto commentDto = commentService.findById(TEST_COMMENT_ID);
            System.out.println(commentDto.commentToString());
        });
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        assertDoesNotThrow(() -> {
            List<CommentDto> commentDto = commentService.findAllByBookId(TEST_BOOK_ID);
            commentDto.forEach(c -> System.out.println(c.commentToString()));
        });
    }

    @DisplayName("должен сохранить новый комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        assertDoesNotThrow(() -> {
            CommentDto commentDto = commentService.create(TEST_COMMENT, TEST_BOOK_ID);
            System.out.println(commentDto.commentToString());
        });
    }

    @DisplayName("должен изменить комментарий")
    @Test
    void shouldUpdateComment() {
        assertDoesNotThrow(() -> {
            CommentDto commentDto = commentService.update(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK_ID);
            System.out.println(commentDto.commentToString());
        });
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertDoesNotThrow(() -> {
            commentService.deleteById(5L);
        });
    }
}