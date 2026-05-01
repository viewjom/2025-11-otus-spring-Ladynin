package ru.otus.hw.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class TelephoneNumberDto {

    private Long id;

    private TariffPlanDto tariffPlan;

    @Size(min = 11, max = 11, message = "The length of Telephone Number must be 11 symbols")
    private String number;

    private Long contractId;
}
