package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookDtoConverter bookConverter;

    @CircuitBreaker(name = "BookBreaker", fallbackMethod = "getBookFallback")
    @Override
    public BookDto findById(long id) {
        return bookConverter
                .getDto(bookRepository.findById(id).get());
    }

    @CircuitBreaker(name = "BookBreaker", fallbackMethod = "getBookListFallback")
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream().map(bookConverter::getDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, long genreId) {
        Book book = save(id, title, authorId, genreId);
        BookDto bookDto = bookConverter.getDto(book);
        LOGGER.info("{} Book: {}",
                id == 0 ? "Created" : "Updated",
                bookDto.toString());
        return bookDto;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        LOGGER.info("Deleted Book: {}", id);
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

    public List<BookDto> getBookListFallback(Exception ex) {
        log.error("Book Fallback:" + ex.getMessage(), ex);
        return null;
    }

    public BookDto getBookFallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return null;
    }
}