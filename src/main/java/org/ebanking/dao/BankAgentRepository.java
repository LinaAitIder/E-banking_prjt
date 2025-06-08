package org.ebanking.dao;

import org.ebanking.model.BankAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAgentRepository extends JpaRepository<BankAgent, Long> {

    Optional<BankAgent> findById(Long id);
}
