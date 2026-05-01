package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CostDto;
import ru.otus.hw.models.Cost;

@RequiredArgsConstructor
@Component
public class CostDtoConverter {

    private final ServiceDtoConverter serviceDtoConverter;

    public CostDto getDto(Cost cost) {
        return new CostDto(cost.getId(),
                serviceDtoConverter.getDto(cost.getServices()),
                cost.getTpt(),
                cost.getCost());
    }
}
