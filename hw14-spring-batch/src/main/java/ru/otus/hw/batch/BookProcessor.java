package ru.otus.hw.batch;

import jakarta.annotation.Nonnull;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoBook;

public class BookProcessor implements ItemProcessor<MongoBook, JpaBook> {
    @Override
    public JpaBook process(@Nonnull MongoBook item) throws Exception {
        AuthorProcessor authorProcessor = new AuthorProcessor();
        JpaAuthor author = authorProcessor.process(item.getAuthor());

        GenreProcessor genreProcessor = new GenreProcessor();
        JpaGenre genre = genreProcessor.process(item.getGenre());

        return new JpaBook(0l, item.getTitle(), author, genre);
    }
}
