package ru.otus.hw.services.mongo;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

@RequiredArgsConstructor
@Service
public class MongoBookServiceImpl implements MongoBookService {

    private final MongoBookRepository bookRepository;

    @Override
    public Optional<MongoBook> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<MongoBook> findAll() {
        return bookRepository.findAll();
    }
}