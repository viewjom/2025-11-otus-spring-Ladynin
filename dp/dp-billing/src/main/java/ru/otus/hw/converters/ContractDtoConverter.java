package ru.otus.hw.converters;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.ContractDto;
import ru.otus.hw.dto.ContractEditDto;
import ru.otus.hw.dto.TelephoneNumberEditDto;
import ru.otus.hw.models.Contract;


@RequiredArgsConstructor
@Component
public class ContractDtoConverter {

    public ContractDto getDto(Contract contract) {
        return new ContractDto(contract.getId(),
                contract.getContractNumber(),
                contract.getName(),
                contract.getAddress());
    }

    public ContractEditDto getDto(Contract contract, List<TelephoneNumberEditDto> telephoneNumbers) {
        return new ContractEditDto(contract.getId(),
                contract.getContractNumber(),
                contract.getName(),
                contract.getAddress(),
                telephoneNumbers);
    }
}