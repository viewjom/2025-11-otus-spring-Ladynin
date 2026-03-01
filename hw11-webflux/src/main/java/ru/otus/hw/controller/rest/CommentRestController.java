package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.converters.CommentDtoConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.repositories.ReactiveCommentRepository;


@RestController
@RequiredArgsConstructor
public class CommentRestController {
    private final ReactiveCommentRepository commentRepository;

    private final CommentDtoConverter commentDtoConverter;

    @GetMapping("/api/comments")
    public Flux<CommentDto> getCommentByBookId(@RequestParam("bookId") String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .map(commentDtoConverter::getDto);
    }
}