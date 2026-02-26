package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter bookConverter;

    @Override
    public BookDto findById(long id) {
        return bookConverter
                .getDto(bookRepository.findById(id).get());
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream().map(bookConverter::getDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDto create(String title, long authorId, long genreId) {
        Book book = save(0, title, authorId, genreId);
        BookDto bookDto = bookConverter.getDto(book);
        return bookDto;
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, long genreId) {
        Book book = save(id, title, authorId, genreId);
        BookDto bookDto = bookConverter.getDto(book);
        return bookDto;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}