package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@RequiredArgsConstructor
@Component
public class BookDtoConverter {
    private final AuthorDtoConverter authorConverter;

    private final GenreDtoConverter genreConverter;

    public BookDto getDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.getDto(book.getAuthor()),
                genreConverter.getDto(book.getGenre()));
    }
}