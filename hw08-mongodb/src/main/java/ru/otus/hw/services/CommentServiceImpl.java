package ru.otus.hw.services;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentDtoConverter commentDtoConverter;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(String id) {
        Comment comment = commentRepository.findById(id).get();
        return commentDtoConverter.getDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream().map(commentDtoConverter::getDto).toList();
    }

    @Transactional
    @Override
    public CommentDto create(String text, String bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        var comment = new Comment(null, text, book);
        commentRepository.save(comment);
        return commentDtoConverter.getDto(comment);
    }

    @Transactional
    @Override
    public CommentDto update(String id, String text, String bookId) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException(
                    "Comment not found");
        }
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        var newComment = new Comment(id, text, book);
        commentRepository.save(newComment);
        return commentDtoConverter.getDto(newComment);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}