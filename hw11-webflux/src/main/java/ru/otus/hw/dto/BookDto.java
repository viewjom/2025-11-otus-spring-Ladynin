package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class BookDto {

    private String id;

    private String title;

    private AuthorDto authorDto;

    private GenreDto genreDto;
}
