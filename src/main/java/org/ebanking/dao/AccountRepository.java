package org.ebanking.dao;

import org.ebanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClient_Id(Long clientId);
    List<Account> findByType(AccountType type);

    @Query("SELECT a FROM Account a JOIN FETCH a.transactions WHERE a.id = :accountId")
    Account findAccountWithTransactions(@Param("accountId") Long accountId);
}