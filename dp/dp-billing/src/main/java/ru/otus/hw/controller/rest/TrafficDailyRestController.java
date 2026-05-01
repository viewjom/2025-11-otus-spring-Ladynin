package ru.otus.hw.controller.rest;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.TrafficDailyDto;
import ru.otus.hw.services.TrafficDailyService;

@RestController
@RequiredArgsConstructor
public class TrafficDailyRestController {

    private final TrafficDailyService trafficDailyService;

    @GetMapping("/api/trafficDaily/contract/{id}")
    public List<TrafficDailyDto> findAllByContractId(@PathVariable long id) {
        return trafficDailyService.findAllByContractId(id);
    }
}