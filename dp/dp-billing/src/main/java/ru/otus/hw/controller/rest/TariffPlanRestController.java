package ru.otus.hw.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.TariffPlanWithCostDto;
import ru.otus.hw.services.TariffPlanService;

@RestController
@RequiredArgsConstructor
public class TariffPlanRestController {

    private final TariffPlanService tariffPlanService;

    @GetMapping("/api/tariffs/{id}")
    public TariffPlanWithCostDto findAllByContractId(@PathVariable long id) {
        return tariffPlanService.findById(id);
    }
}
