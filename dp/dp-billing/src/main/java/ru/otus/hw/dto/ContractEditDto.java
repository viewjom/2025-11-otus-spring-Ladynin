package ru.otus.hw.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ContractEditDto {

    private long id;

    private String contractNumber;

    private String name;

    private String address;

    private List<TelephoneNumberEditDto> telephoneNumber;
}
