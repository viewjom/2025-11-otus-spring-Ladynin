package ru.otus.hw.commands;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(long id) {
        CommentDto dto = commentService.findById(id);
        return dto.commentToString();

    }

    @ShellMethod(value = "Find comments by book_id", key = "cbbid")
    public String findAllForBook(long id) {
        return commentService.findAllForBook(id).stream()
                .map(CommentDto::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 1
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String text, long bookId) {
        var comment = commentService.create(text, bookId);
        return comment.commentToString();
    }

    // cupd 6 editedComment 3
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(long id, String text, long bookId) {
        var comment = commentService.update(id, text, bookId);
        return comment.commentToString();
    }

    // cdel 6
    @ShellMethod(value = "Delete comment", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
