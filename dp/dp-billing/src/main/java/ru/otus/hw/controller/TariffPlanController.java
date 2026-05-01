package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class TariffPlanController {
    @GetMapping("/tariffList")
    public String findAll(Model model) {
        return "tariffList";
    }
}
