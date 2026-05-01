package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.TelephoneNumberDto;

public interface TelephoneNumberService {
    List<TelephoneNumberDto> findAll();

    TelephoneNumberDto findById(long tariffPlanId);

    List<TelephoneNumberDto> findAllByContractId(Long contractId);

    TelephoneNumberDto findByNumber(String number);

    TelephoneNumberDto update(long id, long tariffPlanId, String number, long contractId) ;

    void deleteById(long id);
}