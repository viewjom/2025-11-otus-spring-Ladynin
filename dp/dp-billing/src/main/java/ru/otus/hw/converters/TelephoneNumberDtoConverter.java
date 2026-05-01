package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.TelephoneNumberDto;
import ru.otus.hw.dto.TelephoneNumberEditDto;
import ru.otus.hw.models.TelephoneNumber;

@RequiredArgsConstructor
@Component
public class TelephoneNumberDtoConverter {

    private final TariffPlanDtoConverter tariffPlanDtoConverter;

    public TelephoneNumberDto getDto(TelephoneNumber telephoneNumber) {
        return new TelephoneNumberDto(telephoneNumber.getId(),
                tariffPlanDtoConverter.getDto(telephoneNumber.getTariffPlan()),
                telephoneNumber.getNumber(),
                telephoneNumber.getContract().getId());
    }

    public TelephoneNumberEditDto getEditDto(TelephoneNumber telephoneNumber) {
        return new TelephoneNumberEditDto(telephoneNumber.getId(),
                tariffPlanDtoConverter.getDto(telephoneNumber.getTariffPlan()),
                telephoneNumber.getNumber());
    }
}