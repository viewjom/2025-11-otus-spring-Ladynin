package ru.otus.hw.helper;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

public class Generator {

    public static void initBooks(AuthorRepository authorRepository,
                                 GenreRepository genreRepository,
                                 BookRepository bookRepository,
                                 CommentRepository commentRepository) {

        var author1 = authorRepository.save(new Author("1", "Author_1"));
        var author2 = authorRepository.save(new Author("2", "Author_2"));
        var author3 = authorRepository.save(new Author("3", "Author_3"));

        var genre1 = genreRepository.save(new Genre("1", "Genre_1"));
        var genre2 = genreRepository.save(new Genre("2", "Genre_2"));
        var genre3 = genreRepository.save(new Genre("3", "Genre_3"));

        var book1 = bookRepository.save(new Book("1", "BookTitle_1", author1, genre1));
        var book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, genre2));
        var book3 = bookRepository.save(new Book("3", "BookTitle_2", author3, genre3));

        commentRepository.save(new Comment("1", "Хорошая книга", book1));
        commentRepository.save(new Comment("2", "Очень хорошая книга", book1));
        commentRepository.save(new Comment("3", "Нормальная книга", book2));
        commentRepository.save(new Comment("4", "Прекрасная книга", book2));
        commentRepository.save(new Comment("5", "Великолепная книга", book3));
        commentRepository.save(new Comment("6", "Классная книга", book3));
    }
}