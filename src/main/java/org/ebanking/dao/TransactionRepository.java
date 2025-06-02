package org.ebanking.dao;

import org.ebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Méthodes génériques
}
