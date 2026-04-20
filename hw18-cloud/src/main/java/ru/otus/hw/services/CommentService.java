package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.CommentDto;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllByBookId(long bookId);

    CommentDto create(String text, long bookId);

    CommentDto update(long id, String text, long bookId);

    void deleteById(long id);
}