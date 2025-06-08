package org.ebanking.dao;

import org.ebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN FETCH t.account a " +
            "LEFT JOIN FETCH a.owner " +
            "WHERE t.account.id = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN FETCH t.account a " +
            "LEFT JOIN FETCH a.owner " +
            "WHERE t.account.id = :accountId " +
            "ORDER BY t.transactionDate DESC LIMIT 5")
    List<Transaction> findRecentTransactions(@Param("accountId") Long accountId);
}
