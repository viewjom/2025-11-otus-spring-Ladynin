package ru.otus.hw.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.ContractDtoConverter;
import ru.otus.hw.converters.TelephoneNumberDtoConverter;
import ru.otus.hw.dto.ContractDto;
import ru.otus.hw.dto.ContractEditDto;
import ru.otus.hw.dto.TelephoneNumberEditDto;
import ru.otus.hw.models.Contract;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.repositories.ContractRepository;
import ru.otus.hw.repositories.TelephoneNumberRepository;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {
    private final TelephoneNumberRepository telephoneNumberRepository;

    private final TelephoneNumberDtoConverter telephoneNumberDtoConverter;

    private final ContractRepository contractRepository;

    private final ContractDtoConverter contractConverter;

    private final ContractFilterService contractFilterService;

    private final UserService userService;

    @Override
    public ContractEditDto findById(long id) {

       List<TelephoneNumberEditDto> telephoneNumberDtoList = telephoneNumberRepository
               .findAllByContractId(id)
                .stream().map(telephoneNumberDtoConverter::getEditDto).toList();
        return contractConverter
                .getDto(contractRepository.findById(id).get(), telephoneNumberDtoList);
    }

    @Override
    public List<ContractDto> findAll() {
        List<ContractDto> contract = contractFilterService.findAll()
                .stream().map(contractConverter::getDto).toList();
        return contract;
    }

    @Transactional
    @Override
    public ContractDto update(long id, String contractNumber, String name, String address) {
        Contract contract = save(id, contractNumber, name, address);
        ContractDto contractDto = contractConverter.getDto(contract);
        return contractDto;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        contractRepository.deleteById(id);
    }

    private Contract save(long id, String contractNumber, String name, String address) {
        Contract contract = contractRepository.save(new Contract(id, contractNumber, name, address));
        if (id == 0L) {
            userService.create(contractNumber);
        }
        return contract;
    }
}