package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CostDtoConverter;
import ru.otus.hw.dto.CostDto;
import ru.otus.hw.repositories.CostRepository;

@RequiredArgsConstructor
@Service
public class CostServiceImpl implements CostService {

    private final CostRepository costRepository;

    private final CostDtoConverter costDtoConverter;

    @Override
    public List<CostDto> findAllByTariffPlanId(Long tariffPlanId) {

       return costRepository.findAllByTariffPlanId(tariffPlanId)
               .stream().map(costDtoConverter::getDto).toList();

    }
}
