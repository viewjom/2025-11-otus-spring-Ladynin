package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreDtoConverter genreConverter;

    @Override
    public List<GenreDto> findAll() {
            return genreRepository.findAll()
                    .stream().map(genreConverter::getDto)
                    .collect(Collectors.toList());

    }
}