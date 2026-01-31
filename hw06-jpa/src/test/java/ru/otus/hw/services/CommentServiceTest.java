package ru.otus.hw.services;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentRepository;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({CommentServiceImpl.class, JpaCommentRepository.class, JpaBookRepository.class,
        CommentDtoConverter.class, BookConverter.class, AuthorConverter.class, GenreConverter.class})
class CommentServiceTest {

    @Autowired
    private CommentServiceImpl commentService;

    private static final long TEST_BOOK_ID = 1L;

    private static final long TEST_COMMENT_ID = 3L;

    private static final String TEST_COMMENT = "TEST_COMMENT";

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        CommentDto commentDto = assertDoesNotThrow(() -> {
            return commentService.findById(TEST_COMMENT_ID);
        });
        assertThat(commentDto.getId()).isEqualTo(TEST_COMMENT_ID);
        System.out.println(commentDto.commentToString());
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        List<CommentDto> commentDto = assertDoesNotThrow(() -> {
            return commentService.findAllForBook(TEST_BOOK_ID);
        });
        commentDto.forEach(c -> System.out.println(c.commentToString()));
    }

    @DisplayName("должен сохранить новый комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        CommentDto commentDto = assertDoesNotThrow(() -> {
            return commentService.create(TEST_COMMENT, TEST_BOOK_ID);
        });
        assertThat(commentDto.getText()).isEqualTo(TEST_COMMENT);
        System.out.println(commentDto.commentToString());
    }

    @DisplayName("должен изменить комментарий")
    @Test
    void shouldUpdateComment() {
        CommentDto commentDto = assertDoesNotThrow(() -> {
            return commentService.update(TEST_COMMENT_ID, TEST_COMMENT, TEST_BOOK_ID);
        });
        assertThat(commentDto.getText()).isEqualTo(TEST_COMMENT);
        System.out.println(commentDto.commentToString());
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertDoesNotThrow(() -> {
            commentService.deleteById(TEST_COMMENT_ID);
        });
    }
}