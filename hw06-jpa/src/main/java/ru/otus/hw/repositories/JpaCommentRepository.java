package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Comment> findById(long id) {
        try {
            EntityGraph<?> entityGraph = em
                    .createEntityGraph(Book.class);
            entityGraph.addAttributeNodes("author");
            entityGraph.addAttributeNodes("genre");

            Comment comment = em.find(Comment.class, id,
                    Collections.singletonMap(
                            "javax.persistence.loadgraph",
                            entityGraph));
            return Optional.ofNullable(comment);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findAllForBook(Long bookId) {
        TypedQuery<Comment> query = em.createQuery(
                """ 
                select c 
                  from Comment c                               
                 where c.book.id = :book_id
                """,
                Comment.class);
        query.setParameter("book_id", bookId);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }
}
