package ru.otus.hw.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    //http://localhost:8080/author
    @GetMapping({"/author", "/authors"})
    public String findAllAuthors() {
        return "authorList";
    }
}