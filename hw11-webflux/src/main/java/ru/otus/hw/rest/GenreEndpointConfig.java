package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.ReactiveGenreRepository;

import java.util.Comparator;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@RequiredArgsConstructor
public class GenreEndpointConfig {

    private final ReactiveGenreRepository genreRepository;

    private final GenreDtoConverter genreDtoConverter;

    @Bean
    public RouterFunction<ServerResponse> genreRouterFunction() {
        return route()
                .GET("/api/genres", this::getAll)
                .build();
    }

    private Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<GenreDto> genres = genreRepository.findAll()
                .map(genreDtoConverter::getDto)
                .sort(Comparator.comparing(GenreDto::getName));
        return ok().body(genres, GenreDto.class);
    }
}