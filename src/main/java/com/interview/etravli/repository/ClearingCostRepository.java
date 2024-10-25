package com.interview.etravli.repository;

import com.interview.etravli.models.ClearingCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCost, UUID> {

    Optional<ClearingCost> findByCardIssuingCountry(String cardIssuingCountry);

}
