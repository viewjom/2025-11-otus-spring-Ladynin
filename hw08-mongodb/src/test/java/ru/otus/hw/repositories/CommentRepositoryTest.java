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
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mt;

    private static final String TEST_BOOK_ID = "1";

    private static final String TEST_COMMENT_ID = "2";

    @BeforeEach
    void init() {
        Generator.initBooks(authorRepository, genreRepository, bookRepository, commentRepository);
    }

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComments = mt.findAll(Comment.class);
        for (Comment expectedComment : expectedComments) {
            var actualBook = commentRepository.findById(expectedComment.getId());

            assertThat(actualBook).isPresent()
                    .get()
                    .isEqualTo(expectedComment);
        }
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectCommentsList() {
        var actualComments = commentRepository.findAllByBookId(TEST_BOOK_ID);
        var expectedComments = mt.findAll(Comment.class)
                .stream().filter(b -> b.getBook().getId().equals(TEST_BOOK_ID)).toList();

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
        actualComments.forEach(System.out::println);
    }

    @DisplayName("должен сохранить новый комментарий к книге")
    @Test
    void shouldSaveNewComment() {
        var book = new Book(null, "Test"
                , new Author(null, "Test")
                , new Genre(null, "Test"));
        bookRepository.save(book);
        Comment comment = new Comment(null, "ТЕСТ", book);
        Comment actualComment = commentRepository.save(comment);
        Comment expectedComment = mt.findById(actualComment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен изменить комментарий")
    @Test
    void shouldUpdateComment() {
        var book = new Book(null, "Test"
                , new Author(null, "Test")
                , new Genre(null, "Test"));
        bookRepository.save(book);
        Comment comment = mt.findById("3", Comment.class);
        comment.setText("Test");
        comment.setBook(book);
        var actualComment = commentRepository.save(comment);
        var expectedComment = mt.findById(actualComment.getId(), Comment.class);

        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        Comment delComment = mt.findById(TEST_COMMENT_ID, Comment.class);
        assertThat(delComment).isNotNull();

        commentRepository.deleteById(TEST_COMMENT_ID);
        Comment emptyComment = mt.findById(TEST_COMMENT_ID, Comment.class);
        assertThat(emptyComment).isNull();
    }
}