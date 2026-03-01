package ru.otus.hw.controller.rest;

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
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.ReactiveAuthorRepository;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(AuthorDtoConverter.class)
@WebFluxTest(controllers = AuthorRestController.class)
public class AuthorRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthorDtoConverter authorDtoConverter;

    @MockBean
    private ReactiveAuthorRepository authorRepository;

    @DisplayName("Должен вернуть список авторов")
    @Test
    void shouldGetAllAuthors() {
        var expectedAuthorList = List.of(new Author("1", "Author_1"),
                new Author("2", "Author_2"),
                new Author("3", "Author_3"));

        List<AuthorDto> expectedDtoResult = expectedAuthorList.stream()
                .map(val-> authorDtoConverter.getDto(val)).toList();

        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(expectedAuthorList));

        var result = webTestClient
                .get().uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AuthorDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThatList(result).isEqualTo(expectedDtoResult);
    }
}
