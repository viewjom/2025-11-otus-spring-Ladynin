package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.models.Book;

public interface BookFilterService {

    List<Book> findAll();
}
