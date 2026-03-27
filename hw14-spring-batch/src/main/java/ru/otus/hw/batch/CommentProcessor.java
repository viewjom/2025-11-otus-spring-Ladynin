package ru.otus.hw.batch;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;

import ru.otus.hw.models.mongo.MongoComment;

@Component
@RequiredArgsConstructor
public class CommentProcessor implements ItemProcessor<MongoComment, JpaComment> {

    private final BatchCache cache;

    @Override
    public JpaComment process(@Nonnull MongoComment item) throws Exception {
        JpaBook jpaBook = (JpaBook) cache.get(item.getBook().getId());
        return new JpaComment(0l, item.getText(), jpaBook);
    }
}
