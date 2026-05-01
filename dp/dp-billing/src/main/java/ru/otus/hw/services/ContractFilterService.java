package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.models.Contract;

public interface ContractFilterService {

    List<Contract> findAll();
}
