package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PagesController {

    @PostMapping("/fail")
    public String failPage(Model model) {
        model.addAttribute("source", "failPage");
        return "error";
    }
}
