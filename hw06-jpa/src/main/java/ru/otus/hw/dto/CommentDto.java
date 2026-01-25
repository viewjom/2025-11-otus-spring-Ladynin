package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Book;

@Getter
@Setter
public class CommentDto {
    private final BookConverter bookConverter;

    private long id;

    private String text;

    private Book book;

    public CommentDto(BookConverter bookConverter) {
        this.bookConverter = bookConverter;
    }

    public String commentToString() {
        return "Id: %d, Text: %s, book_id: {%s}"
                .formatted(
                        this.id,
                        this.text,
                        bookConverter.bookToString(this.book)
                );
    }
}
