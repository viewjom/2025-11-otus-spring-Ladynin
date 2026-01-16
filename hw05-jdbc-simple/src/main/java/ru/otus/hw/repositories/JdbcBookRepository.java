package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {
    private static final String BOOK_NOT_FOUND = "Book not found";

    private final NamedParameterJdbcTemplate jdbc;

    public JdbcBookRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Book> findById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        try {
            Book book = jdbc.queryForObject(
                    """
                            select b.id, b.title, b.author_id, b.genre_id,
                                   a.id, a.full_name,
                                   g.id, g.name
                              from books b
                        inner join authors a on a.id = b.author_id
                        inner join genres g on g.id = b.genre_id
                             where b.id = :id
                            """,
                    params, new BookRowMapper());
            return Optional.of(book);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query(
                """
                        select b.id, b.title, b.author_id, b.genre_id,
                               a.id, a.full_name,
                               g.id, g.name
                          from books b
                    inner join authors a on a.id = b.author_id
                    inner join genres g on g.id = b.genre_id
                        """,
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        int result = jdbc.update("""
                delete books                     
                 where id = :id
                """, params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        jdbc.update("""
                insert into books (title, author_id, genre_id)
                values (:title, :author_id, :genre_id)
                """, params, keyHolder, new String[]{"id"});

        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());
        params.addValue("genre_id", book.getGenre().getId());
        int result = jdbc.update("""
                update books
                   set title = :title, author_id = :author_id, genre_id = :genre_id  
                 where id = :id
                """, params);
        if (result <= 0) {
            throw new EntityNotFoundException(BOOK_NOT_FOUND);
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("books.id");
            String title = rs.getString("books.title");

            Author author = new Author();
            author.setId(rs.getLong("authors.id"));
            author.setFullName(rs.getString("authors.full_name"));

            Genre genre = new Genre();
            genre.setId(rs.getLong("genres.id"));
            genre.setName(rs.getString("genres.name"));
            return new Book(id, title, author, genre);
        }
    }
}