package org.ebanking.dao;

import org.ebanking.model.Transaction;
import org.ebanking.model.Transfer;
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
            "LEFT JOIN FETCH a.owner o " +
            "WHERE a.owner.id = :clientId " +
            "ORDER BY t.transactionDate DESC LIMIT 5")

    List<Transaction> findRecentTransactions(@Param("clientId") Long clientId);

    // Dans TransactionRepository.java
    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.account a LEFT JOIN FETCH a.owner WHERE a.owner.id = :clientId")
    List<Transaction> findOutgoingTransactions(@Param("clientId") Long clientId);

    @Query("SELECT t FROM Transfer t WHERE t.destinationAccount IN (SELECT acc.accountNumber FROM Account acc WHERE acc.owner.id = :clientId)")
    List<Transfer> findIncomingTransfers(@Param("clientId") Long clientId);


}
