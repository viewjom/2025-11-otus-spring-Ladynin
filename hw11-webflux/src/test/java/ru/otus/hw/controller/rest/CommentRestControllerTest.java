package ru.otus.hw.controller.rest;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveCommentRepository;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(CommentDtoConverter.class)
@WebFluxTest(controllers = CommentRestController.class)
public class CommentRestControllerTest {

    private static final String TEST_ID = "1";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private CommentDtoConverter commentDtoConverter;

    @MockBean
    private ReactiveCommentRepository commentRepository;

    @DisplayName("Должен вернуть комментарии для книги")
    @Test
    void shouldGetCommentByBook() {
        var expectedCommentList = getTestListComment();

        List<CommentDto> expectedDtoResult = expectedCommentList.stream()
                .map(val -> commentDtoConverter.getDto(val)).toList();

        when(commentRepository.findAllByBookId(TEST_ID)).thenReturn(Flux.fromIterable(expectedCommentList));

        var result = webTestClient
                .get().uri("/api/comments?bookId=" + TEST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(CommentDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThatList(result).isEqualTo(expectedDtoResult);
    }
    private List<Comment> getTestListComment() {
        Book book1 = new Book(TEST_ID, "BookTitle_1",
                new Author(TEST_ID, "Author_1"),
                new Genre(TEST_ID, "Genre_1"));
        Comment comment1 = new Comment(TEST_ID, "Хорошая книга", book1);
        Comment comment2 = new Comment("2", "Очень хорошая книга", book1);
        return Arrays.asList(comment1, comment2);
    }
}
