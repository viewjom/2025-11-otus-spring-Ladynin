package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.ReactiveAuthorRepository;
import ru.otus.hw.repositories.ReactiveBookRepository;
import ru.otus.hw.repositories.ReactiveCommentRepository;
import ru.otus.hw.repositories.ReactiveGenreRepository;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropTables", author = "ladynin", runAlways = true)
    public void dropTables(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "addBooks", author = "ladynin")
    public void addBooks(ReactiveAuthorRepository authorRepository,
                         ReactiveGenreRepository genreRepository,
                         ReactiveBookRepository bookRepository,
                         ReactiveCommentRepository commentRepository) {

        var author1 = authorRepository.save(new Author("1", "Author_1")).block();
        var author2 = authorRepository.save(new Author("2", "Author_2")).block();
        var author3 = authorRepository.save(new Author("3", "Author_3")).block();

        var genre1 = genreRepository.save(new Genre("1", "Genre_1")).block();
        var genre2 = genreRepository.save(new Genre("2", "Genre_2")).block();
        var genre3 = genreRepository.save(new Genre("3", "Genre_3")).block();

        Book book1 = bookRepository.save(new Book("1", "BookTitle_1", author1 , genre1)).block();
        Book book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, genre2)).block();
        Book book3 = bookRepository.save(new Book("3", "BookTitle_3", author3, genre3)).block();

        commentRepository.save(new Comment("1", "Хорошая книга", book1)).block();
        commentRepository.save(new Comment("2", "Очень хорошая книга", book1)).block();
        commentRepository.save(new Comment("3", "Нормальная книга", book2)).block();
        commentRepository.save(new Comment("4", "Прекрасная книга", book2)).block();
        commentRepository.save(new Comment("5", "Великолепная книга", book3)).block();
        commentRepository.save(new Comment("6", "Классная книга", book3)).block();
    }
}