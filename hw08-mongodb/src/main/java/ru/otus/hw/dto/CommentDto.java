package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.converters.BookConverter;

@Getter
@Setter
public class CommentDto {
    private final BookConverter bookConverter;

    private String id;

    private String text;

    public CommentDto(BookConverter bookConverter) {
        this.bookConverter = bookConverter;
    }

    public String commentToString() {
        return "Id: %s, Text: %s"
                .formatted(
                        this.id,
                        this.text
                );
    }
}
