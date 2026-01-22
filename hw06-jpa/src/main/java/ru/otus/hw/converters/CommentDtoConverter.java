package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentDtoConverter {
    private final BookConverter bookConverter;

    public CommentDto getDto(Comment comment) {
        CommentDto dto = new CommentDto(bookConverter);
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setBook(comment.getBook());
        return dto;
    }
}
