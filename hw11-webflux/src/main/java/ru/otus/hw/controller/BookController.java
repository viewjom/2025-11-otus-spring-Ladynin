package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BookController {

    //http://localhost:8080/book
    @GetMapping({"/", "/books"})
    public String findAllBooks() {
        return "bookList";
    }

    @GetMapping("/bookEdit")
    public String editBook() {
        return "bookEdit";
    }

}