package ru.otus.hw.dto;

import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.models.Book;


public class CommentDto {
    private final BookConverter bookConverter;

    private long id;

    private String text;

    private Book book;

    public CommentDto(BookConverter bookConverter) {
        this.bookConverter = bookConverter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String commentToString() {
        return "Id: %d, Text: %s, book: {%s}"
                .formatted(
                        this.id,
                        this.text,
                        bookConverter.bookToString(this.book)
                );
    }
}
