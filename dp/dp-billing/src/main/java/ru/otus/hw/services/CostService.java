package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.CostDto;

public interface CostService {
    List<CostDto> findAllByTariffPlanId(Long tariffPlanId);
}
