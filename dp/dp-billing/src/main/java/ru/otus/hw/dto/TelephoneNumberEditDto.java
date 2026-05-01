package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class TelephoneNumberEditDto {

        private Long id;

        private TariffPlanDto tariffPlan;

        private String number;
}