package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.TariffPlanDto;
import ru.otus.hw.dto.TariffPlanWithCostDto;

public interface TariffPlanService {

    TariffPlanWithCostDto findById(long tariffPlanId);

    List<TariffPlanDto> findAll();
}
