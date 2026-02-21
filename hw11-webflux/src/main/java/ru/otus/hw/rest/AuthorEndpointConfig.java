package ru.otus.hw.rest;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.ReactiveAuthorRepository;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class AuthorEndpointConfig {

    private final ReactiveAuthorRepository authorRepository;

    private final AuthorDtoConverter authorDtoConverter;

        @Bean
        public RouterFunction<ServerResponse> authorRouterFunction() {
            return route()
                    .GET("/api/authors", this::getAll)
                    .build();
        }

        public Mono<ServerResponse> getAll(ServerRequest request) {
            Flux<AuthorDto> authors = authorRepository.findAll()
                    .map(authorDtoConverter::getDto)
                    .sort(Comparator.comparing(AuthorDto::getFullName));
            return ok().body(authors, AuthorDto.class);
        }
}
