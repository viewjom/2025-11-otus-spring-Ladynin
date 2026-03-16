package ru.otus.hw.repositories.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaBook;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {
    @EntityGraph("book-graph")
    List<JpaBook> findAll();

    Optional<JpaBook> findByTitle(String title);

    @EntityGraph("book-graph")
    Optional<JpaBook> findById(Long id);
}