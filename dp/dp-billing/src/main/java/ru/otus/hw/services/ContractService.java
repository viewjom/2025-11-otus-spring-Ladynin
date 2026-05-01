package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.ContractDto;
import ru.otus.hw.dto.ContractEditDto;

public interface ContractService {
    ContractEditDto findById(long id);

    List<ContractDto> findAll();

    ContractDto update(long id, String contractNumber, String name, String address);

    void deleteById(long id);
}