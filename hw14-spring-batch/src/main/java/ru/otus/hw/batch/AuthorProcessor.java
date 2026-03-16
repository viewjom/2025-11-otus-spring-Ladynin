package ru.otus.hw.batch;

import jakarta.annotation.Nonnull;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.mongo.MongoAuthor;

public class AuthorProcessor implements ItemProcessor<MongoAuthor, JpaAuthor> {
    @Override
    public JpaAuthor process(@Nonnull MongoAuthor item) throws Exception {

        return new JpaAuthor(0L, item.getFullName());
    }
}
