package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.ServiceDtoConverter;
import ru.otus.hw.dto.ServiceDto;
import ru.otus.hw.repositories.ServiceRepository;

@RequiredArgsConstructor
@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceDtoConverter serviceDtoConverter;

    @Override
    public ServiceDto findById(long id) {
        return serviceDtoConverter.getDto(serviceRepository.findById(id).get());
    }
}
