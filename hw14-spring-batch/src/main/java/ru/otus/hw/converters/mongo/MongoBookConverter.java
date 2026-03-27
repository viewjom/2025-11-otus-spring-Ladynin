package ru.otus.hw.converters.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.mongo.MongoBook;

@RequiredArgsConstructor
@Component
public class MongoBookConverter {
    private final MongoAuthorConverter authorConverter;

    private final MongoGenreConverter genreConverter;

    public String bookToString(MongoBook book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()));
    }
}