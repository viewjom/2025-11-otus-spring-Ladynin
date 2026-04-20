package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreDtoConverter genreConverter;

    @CircuitBreaker(name = "GenreBreaker", fallbackMethod = "getGenreListFallback")
    @Override
    public List<GenreDto> findAll() {
            return genreRepository.findAll()
                    .stream().map(genreConverter::getDto)
                    .collect(Collectors.toList());

    }

    public List<GenreDto> getGenreListFallback(Exception ex) {
        log.error("Author Fallback:" + ex.getMessage(), ex);
        return null;
    }
}