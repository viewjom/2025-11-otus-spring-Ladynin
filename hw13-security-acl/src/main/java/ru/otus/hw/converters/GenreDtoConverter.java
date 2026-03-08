package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreDtoConverter {
    public GenreDto getDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}