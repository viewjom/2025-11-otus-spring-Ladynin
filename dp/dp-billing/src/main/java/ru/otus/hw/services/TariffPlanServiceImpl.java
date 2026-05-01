package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.TariffPlanDtoConverter;
import ru.otus.hw.dto.CostDto;
import ru.otus.hw.dto.TariffPlanDto;
import ru.otus.hw.dto.TariffPlanWithCostDto;
import ru.otus.hw.repositories.TariffPlanRepositiry;

@RequiredArgsConstructor
@Service
public class TariffPlanServiceImpl implements  TariffPlanService {

    private final TariffPlanRepositiry tariffPlanRepositiry;

    private final TariffPlanDtoConverter tariffPlanDtoConverter;

    private final CostService costService;

    @Override
    public TariffPlanWithCostDto findById(long tariffPlanId) {
        List<CostDto> costs = costService.findAllByTariffPlanId(tariffPlanId);
        return tariffPlanDtoConverter.getDto(tariffPlanRepositiry.findById(tariffPlanId).get(), costs);
    }

    @Override
    public List<TariffPlanDto> findAll() {
        return tariffPlanRepositiry.findAll().stream().map(tariffPlanDtoConverter::getDto).toList();
    }
}
