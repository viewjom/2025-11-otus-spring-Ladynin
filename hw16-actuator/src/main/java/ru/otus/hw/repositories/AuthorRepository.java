package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select count(1) from Author a where " +
            "not exists (select 1 from Book b where b.author = a)")
    int findErrorAuthors();
}