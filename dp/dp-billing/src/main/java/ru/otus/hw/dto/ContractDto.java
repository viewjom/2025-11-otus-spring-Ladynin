package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ContractDto {

    public static final ContractDto BOOK_DTO_EMPTY = new ContractDto(0L, null,null, null);

    private long id;

    @NotBlank(message = "The contract number can't be empty")
    private String contractNumber;

    @NotBlank(message = "The name can't be empty")
    private String name;

    @NotBlank(message = "The address can't be empty")
    private String address;
}
