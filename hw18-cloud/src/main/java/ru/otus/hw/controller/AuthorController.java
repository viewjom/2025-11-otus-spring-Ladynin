package ru.otus.hw.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    //http://localhost:8080/author
    @GetMapping({"/author", "/authors"})
    public String findAllAuthors(Model model) {
        List<AuthorDto> authorList = authorService.findAll();
        model.addAttribute("authors", authorList);
        return "authorList";
    }
}