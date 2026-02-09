package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Long id;

    private String title;

    private AuthorDto authorDto;

    private GenreDto genreDto;
}
