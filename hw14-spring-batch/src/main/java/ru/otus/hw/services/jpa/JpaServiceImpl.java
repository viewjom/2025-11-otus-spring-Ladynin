package ru.otus.hw.services.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;

@Service
@RequiredArgsConstructor
public class JpaServiceImpl implements JpaService {

    private final JpaBookRepository jpaBookRepository;

    private final JpaCommentRepository jpaCommentRepository;

    @Override
    public JpaComment prepareJpaComment(MongoComment comment) {

        JpaBook book = jpaBookRepository
                .findByTitle(comment.getBook().getTitle()).get();
        return new JpaComment(0L, comment.getText(), book);
    }
}
