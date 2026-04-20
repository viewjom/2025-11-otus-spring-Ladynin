package ru.otus.hw.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    //http://localhost:8080/comment?id=1
    @GetMapping("/comment")
    public String findAllForBook(@RequestParam("id") long id, Model model) {
        List<CommentDto> comments = commentService.findAllByBookId(id);
        model.addAttribute("comments", comments);

        return "commentList";
    }
}
