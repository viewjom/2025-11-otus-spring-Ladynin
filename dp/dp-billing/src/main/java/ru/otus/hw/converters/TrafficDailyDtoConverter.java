package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.TrafficDailyDto;
import ru.otus.hw.models.TrafficDaily;

@RequiredArgsConstructor
@Component
public class TrafficDailyDtoConverter {

    private final TelephoneNumberDtoConverter telephoneNumberDtoConverter;

    private final ServiceDtoConverter serviceDtoConverter;

    public TrafficDailyDto getDto(TrafficDaily trafficDaily) {
        TrafficDailyDto dto = new TrafficDailyDto(trafficDaily.getId(),
                trafficDaily.getCdate(),
                serviceDtoConverter.getDto(trafficDaily.getService()),
                telephoneNumberDtoConverter.getEditDto(trafficDaily.getTelephoneNumber()),
                trafficDaily.getBnumber(),
                trafficDaily.getDuration(),
                trafficDaily.getAmount());
        return dto;
    }
}
