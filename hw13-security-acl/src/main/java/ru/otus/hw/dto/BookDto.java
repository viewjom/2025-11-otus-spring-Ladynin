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

    public static final BookDto BOOK_DTO_EMPTY = new BookDto(0L, null,
            new AuthorDto(0L, null),
            new GenreDto(0L, null));

    private long id;

    private String title;

    private AuthorDto authorDto;

    private GenreDto genreDto;
}
