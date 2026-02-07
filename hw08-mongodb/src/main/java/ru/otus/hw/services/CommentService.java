package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.CommentDto;

public interface CommentService {
    CommentDto findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto create(String text, String bookId);

    CommentDto update(String id, String text, String bookId);

    void deleteById(String id);
}