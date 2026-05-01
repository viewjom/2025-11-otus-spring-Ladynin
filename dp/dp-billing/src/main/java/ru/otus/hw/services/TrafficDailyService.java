package ru.otus.hw.services;

import java.time.LocalDateTime;
import java.util.List;
import ru.otus.hw.dto.TrafficDailyDto;

public interface TrafficDailyService {

    TrafficDailyDto create(LocalDateTime cdate, String anumber, String bnumber, double duration);

    List<TrafficDailyDto> findAllByContractId(long contractId);

    void deleteById(long id);
}