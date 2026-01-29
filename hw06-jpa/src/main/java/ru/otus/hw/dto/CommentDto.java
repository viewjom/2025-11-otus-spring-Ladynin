package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.converters.BookConverter;

@Getter
@Setter
public class CommentDto {
    private final BookConverter bookConverter;

    private long id;

    private String text;

    public CommentDto(BookConverter bookConverter) {
        this.bookConverter = bookConverter;
    }

    public String commentToString() {
        return "Id: %d, Text: %s"
                .formatted(
                        this.id,
                        this.text
                );
    }
}
