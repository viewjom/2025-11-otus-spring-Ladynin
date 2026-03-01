package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.Book;

public interface ReactiveBookRepository extends ReactiveMongoRepository<Book, String> {
}
