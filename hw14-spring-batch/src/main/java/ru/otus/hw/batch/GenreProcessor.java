package ru.otus.hw.batch;

import jakarta.annotation.Nonnull;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoGenre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<MongoGenre, JpaGenre> {

    @Override
    public JpaGenre process(@Nonnull MongoGenre item) throws Exception {
        return new JpaGenre(0l, item.getName(), item.getId());
    }
}
