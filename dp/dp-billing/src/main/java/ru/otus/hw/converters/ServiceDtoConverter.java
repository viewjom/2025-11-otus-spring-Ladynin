package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.ServiceDto;
import ru.otus.hw.models.Service;

@RequiredArgsConstructor
@Component
public class ServiceDtoConverter {

    public ServiceDto getDto(Service service) {

        return new ServiceDto(service.getId(), service.getName());
    }

}
