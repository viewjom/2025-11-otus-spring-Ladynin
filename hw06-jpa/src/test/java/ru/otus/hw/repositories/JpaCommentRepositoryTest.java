package ru.otus.hw.repositories;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager tem;

    private List<Comment> dbComment;

    private static final long TEST_BOOK_ID = 1L;

    private static final long TEST_COMMENT_ID = 2L;

    @BeforeEach
    void setUp() {
        dbComment = getDbComment();
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedBooks = getDbComment();
        for (Comment expectedBook : expectedBooks) {
            var actualBook = jpaCommentRepository.findById(expectedBook.getId());

            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedBook);
        }
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualBooks = jpaCommentRepository.findAllForBook(TEST_BOOK_ID);
        var expectedBooks = dbComment
                .stream().filter(b -> b.getBook().getId() == TEST_BOOK_ID).toList();

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранить новый комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        var book = new Book(0L, "Test"
                , new Author(0L, "Test")
                , new Genre(0L, "Test"));
        Comment comment = new Comment(0L, "ТЕСТ", book);
        Comment actualComment = jpaCommentRepository.save(comment);
        Comment expectedComment = tem.find(Comment.class, actualComment.getId());

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен изменить комментарий")
    @Test
    void shouldUpdateComment() {
        Comment comment = tem.find(Comment.class, 3L);
        comment.setText("Test");
        comment.setBook(new Book(0L, "Test"
                , new Author(0L, "Test")
                , new Genre(0L, "Test")));
        var actualComment = jpaCommentRepository.save(comment);
        var expectedComment = tem.find(Comment.class, actualComment.getId());

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        Comment delComment = tem.find(Comment.class, TEST_COMMENT_ID);
        assertThat(delComment).isNotNull();

        jpaCommentRepository.deleteById(TEST_COMMENT_ID);
        Comment emptyComment = tem.find(Comment.class, TEST_COMMENT_ID);
        assertThat(emptyComment).isNull();
    }

    private List<Comment> getDbComment() {

        return IntStream.range(1, 6).boxed()
                .map(id -> tem.find(Comment.class, id))
                .toList();
    }
}