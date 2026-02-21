package ru.otus.hw.rest;

import java.util.Comparator;
import org.springframework.context.annotation.Bean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunction;
import ru.otus.hw.repositories.ReactiveCommentRepository;

@Configuration
@RequiredArgsConstructor
public class CommentEndpointConfig {

    private final ReactiveCommentRepository commentRepository;

    private final CommentDtoConverter commentDtoConverter;

    @Bean
    public RouterFunction<ServerResponse> commentRouterFunction() {
        return route()
                .GET("/api/comments",  queryParam("bookId", param -> param != null && !param.isEmpty()),
                        request -> request.queryParam("bookId")
                                .map(bookId -> ok().body(commentRepository.findAllByBookId(bookId)
                                                .map(commentDtoConverter::getDto)
                                                .sort(Comparator.comparing(CommentDto::getText))
                                        , CommentDto.class))
                                .orElse(badRequest().build()))
                .build();
    }
}
