package ru.otus.hw.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.TrafficDaily;

public interface TrafficDailyRepository extends JpaRepository<TrafficDaily, Long> {

    @Query("from TrafficDaily td " +
            "join td.telephoneNumber.contract ttc where ttc.id = :contractId ")
    List<TrafficDaily> findAllByContractId(@Param("contractId") Long contractId);
}
