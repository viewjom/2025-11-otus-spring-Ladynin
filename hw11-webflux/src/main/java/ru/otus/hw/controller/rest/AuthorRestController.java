package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.ReactiveAuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final ReactiveAuthorRepository authorRepository;

    private final AuthorDtoConverter authorDtoConverter;

    @GetMapping("/api/authors")
    public Flux<AuthorDto> allAuthorsList() {
        return authorRepository.findAll()
                .map(authorDtoConverter::getDto);
    }
}