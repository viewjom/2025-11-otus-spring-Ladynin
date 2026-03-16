package ru.otus.hw.repositories.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.MongoComment;

public interface MongoCommentRepository extends MongoRepository<MongoComment, String> {
    List<MongoComment> findAllByBookId(String bookId);

    void deleteAllByBookId(String bookId);
}
