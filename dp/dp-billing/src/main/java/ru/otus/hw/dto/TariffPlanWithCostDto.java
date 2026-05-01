package ru.otus.hw.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class TariffPlanWithCostDto {
    private long id;

    private String name;

    private List<CostDto> costs;
}
