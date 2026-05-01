package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import ru.otus.hw.models.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findAll();

    Optional<Contract> findById(Long id);
}