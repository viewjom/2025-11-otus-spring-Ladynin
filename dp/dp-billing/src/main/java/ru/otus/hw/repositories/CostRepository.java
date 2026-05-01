package ru.otus.hw.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Cost;

public interface CostRepository extends JpaRepository<Cost, Long> {

    List<Cost> findAllByTariffPlanId(Long tariffPlanId);
}
