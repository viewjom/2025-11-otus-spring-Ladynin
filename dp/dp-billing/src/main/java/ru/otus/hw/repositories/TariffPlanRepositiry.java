package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.TariffPlan;

public interface TariffPlanRepositiry extends JpaRepository<TariffPlan, Long> {
}
