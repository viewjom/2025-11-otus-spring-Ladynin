package ru.otus.hw.converters;

import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Component
@EqualsAndHashCode
public class GenreDtoConverter {
    public GenreDto getDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toModel(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}