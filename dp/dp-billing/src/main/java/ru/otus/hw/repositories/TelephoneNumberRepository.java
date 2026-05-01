package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.TelephoneNumber;

public interface TelephoneNumberRepository  extends JpaRepository<TelephoneNumber, Long> {
    @EntityGraph("telephone_numbers-graph")
    List<TelephoneNumber> findAllByContractId(Long contractId);

    @EntityGraph("telephone_numbers-graph")
    @Query("from TelephoneNumber tn join fetch tn.tariffPlan tnt join fetch tnt.costs c join fetch c.services" +
            " where tn.number = :number")
    Optional<TelephoneNumber>  findByNumber(@Param("number") String number);
}