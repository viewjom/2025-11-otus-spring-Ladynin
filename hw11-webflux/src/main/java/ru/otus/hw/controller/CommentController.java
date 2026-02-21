package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class CommentController {


    //http://localhost:8080/comment?id=1
    @GetMapping("/comments")
    public String findAllForBook() {
        return "commentList";
    }
}
