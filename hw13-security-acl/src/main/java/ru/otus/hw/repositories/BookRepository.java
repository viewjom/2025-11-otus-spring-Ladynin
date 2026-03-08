package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository  extends JpaRepository<Book, Long> {
    @EntityGraph("book-graph")
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    @EntityGraph("book-graph")
    Optional<Book> findById(Long id);
}