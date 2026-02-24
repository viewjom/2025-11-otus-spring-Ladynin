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
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveGenreRepository;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import(GenreDtoConverter.class)
@WebFluxTest(controllers = GenreRestController.class)
public class GenreRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GenreDtoConverter genreDtoConverter;

    @MockBean
    private ReactiveGenreRepository genreRepository;

    @DisplayName("Должен вернуть список данров")
    @Test
    void shouldGetAllGenre() {
        var expectedGenreList = List.of(new Genre("1", "Genre_1"),
                new Genre("2", "Genre_2"),
                new Genre("3", "Genre_3"));

        List<GenreDto> expectedDtoResult = expectedGenreList.stream()
                .map(val -> genreDtoConverter.getDto(val)).toList();

        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(expectedGenreList));

        var result = webTestClient
                .get().uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(GenreDto.class)
                .getResponseBody()
                .collectList()
                .block();

        assertThatList(result).isEqualTo(expectedDtoResult);
    }
}

