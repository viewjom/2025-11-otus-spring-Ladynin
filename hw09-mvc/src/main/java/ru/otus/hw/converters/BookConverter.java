package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto getDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.getDto(book.getAuthor()),
                genreConverter.getDto(book.getGenre()));
    }

    public BookDto getDto() {
        Book book = new Book(0l, null,
                new Author(0l, null),
                new Genre(0l, null));

        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.getDto(book.getAuthor()),
                genreConverter.getDto(book.getGenre()));
    }
}