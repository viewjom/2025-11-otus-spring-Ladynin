package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.TelephoneNumberDtoConverter;
import ru.otus.hw.dto.TelephoneNumberDto;
import java.util.List;
import ru.otus.hw.models.TelephoneNumber;
import ru.otus.hw.repositories.ContractRepository;
import ru.otus.hw.repositories.TariffPlanRepositiry;
import ru.otus.hw.repositories.TelephoneNumberRepository;

@RequiredArgsConstructor
@Service
public class TelephoneNumberServiceImpl implements TelephoneNumberService {

    private final TelephoneNumberRepository telephoneNumberRepository;

    private final TariffPlanRepositiry tariffPlanRepositiry;

    private final ContractRepository contractRepository;

    private final TelephoneNumberDtoConverter telephoneNumberDtoConverter;

    @Override
    public List<TelephoneNumberDto> findAll() {
        return telephoneNumberRepository.findAll()
                .stream().map(telephoneNumberDtoConverter::getDto)
                .toList();
    }


    @Override
    public TelephoneNumberDto findByNumber(String number) {
        return telephoneNumberDtoConverter.getDto(telephoneNumberRepository.findByNumber(number).get());
    }

    @Override
    public TelephoneNumberDto findById(long tariffPlanId) {
        return telephoneNumberDtoConverter.getDto(telephoneNumberRepository.findById(tariffPlanId).get());
    }

    @Override
    public List<TelephoneNumberDto> findAllByContractId(Long contractId) {
        return telephoneNumberRepository.findAllByContractId(contractId)
                .stream().map(telephoneNumberDtoConverter::getDto).toList();
    }

    @Override
    public TelephoneNumberDto update(long id, long tariffPlanId, String number, long contractId) {
        TelephoneNumber telephoneNumber = telephoneNumberRepository
                .save(new TelephoneNumber(id,
                        tariffPlanRepositiry.findById(tariffPlanId).get(),
                        number,
                        contractRepository.findById(contractId).get()));

        return telephoneNumberDtoConverter.getDto(telephoneNumber);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        telephoneNumberRepository.deleteById(id);
    }
}