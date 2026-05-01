package ru.otus.hw.converters;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CostDto;
import ru.otus.hw.dto.TariffPlanDto;
import ru.otus.hw.dto.TariffPlanWithCostDto;
import ru.otus.hw.models.TariffPlan;

@RequiredArgsConstructor
@Component
public class TariffPlanDtoConverter {

    public TariffPlanDto getDto(TariffPlan tariffPlan) {

        return new TariffPlanDto(tariffPlan.getId(), tariffPlan.getName());
    }

    public TariffPlanWithCostDto getDto(TariffPlan tariffPlan, List<CostDto> costs) {

        return new TariffPlanWithCostDto(tariffPlan.getId(), tariffPlan.getName(), costs);
    }
}
