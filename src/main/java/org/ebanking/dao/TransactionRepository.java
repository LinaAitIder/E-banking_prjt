package org.ebanking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ebanking.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccount_Id(Long accountId);
    List<Transaction> findByDestinationAccount_Id(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findByTypeAndDateBetween(
            @Param("type") TransactionType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}