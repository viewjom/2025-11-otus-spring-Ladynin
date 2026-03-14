package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class BookFilterServiceImpl implements BookFilterService {

    private final BookRepository bookRepository;

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
