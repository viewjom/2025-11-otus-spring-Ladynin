package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {

    public static final BookDto BOOK_DTO_EMPTY = new BookDto(0L, null,
            new AuthorDto(0L, null),
            new GenreDto(0L, null));

    private long id;

    @NotBlank(message = "The title of book can't be empty")
    @Size(min = 2, max = 20, message = "The length of book must be from 2 to 20 symbols")
    private String title;

    private AuthorDto authorDto;

    private GenreDto genreDto;
}
