package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.ReactiveGenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final ReactiveGenreRepository genreRepository;

    private final GenreDtoConverter genreDtoConverter;

    @GetMapping("/api/genres")
    public Flux<GenreDto> allGenresList() {
        return genreRepository.findAll()
                .map(genreDtoConverter::getDto);
    }
}