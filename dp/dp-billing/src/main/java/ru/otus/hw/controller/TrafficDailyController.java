package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.services.TrafficDailyService;

@RequiredArgsConstructor
@Controller
public class TrafficDailyController {

    private final TrafficDailyService trafficDailyService;

    //http://localhost:8080/trafficDaily?id=1
    @GetMapping("/trafficDaily")
    public String find(Model model) {
                return "trafficDaily";
    }
}
