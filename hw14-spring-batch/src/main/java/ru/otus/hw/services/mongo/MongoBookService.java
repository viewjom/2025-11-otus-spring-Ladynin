package ru.otus.hw.services.mongo;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.mongo.MongoBook;

public interface MongoBookService {

    Optional<MongoBook> findById(String id);

    List<MongoBook> findAll();
}