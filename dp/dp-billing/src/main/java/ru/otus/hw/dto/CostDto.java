package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class CostDto {

    private long id;

    private ServiceDto service;

    private String tpt;

    private  double cost;
}
