package ru.otus.hw.services;

import ru.otus.hw.dto.ServiceDto;

public interface ServiceService {

    ServiceDto findById(long id);
}