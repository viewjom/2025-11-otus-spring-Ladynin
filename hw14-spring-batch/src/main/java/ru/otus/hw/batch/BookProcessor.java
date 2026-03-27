package ru.otus.hw.batch;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoBook;

@Component
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<MongoBook, JpaBook> {

    private final BatchCache cache;
    @Override
    public JpaBook process(@Nonnull MongoBook item) throws Exception {
        JpaAuthor jpaAuthor = (JpaAuthor) cache.get(item.getAuthor().getId());
        JpaGenre jpaGenre = (JpaGenre) cache.get(item.getGenre().getId());
        return new JpaBook(0l, item.getTitle(), jpaAuthor, jpaGenre, item.getId());
    }
}