package ru.otus.hw.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrafficDailyDto {

    private long id;

    private LocalDateTime cdate;

    private ServiceDto service;

    private TelephoneNumberEditDto telephoneNumber;

    private String bnumber;

    private double duration;

    private double amount;
}