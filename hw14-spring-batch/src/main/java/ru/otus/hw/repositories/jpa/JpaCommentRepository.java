package ru.otus.hw.repositories.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaComment;

public interface JpaCommentRepository extends JpaRepository<JpaComment, Long> {
    List<JpaComment> findAllByBookId(Long bookId);

    @EntityGraph("comment-graph")
    List<JpaComment> findAll();
}
