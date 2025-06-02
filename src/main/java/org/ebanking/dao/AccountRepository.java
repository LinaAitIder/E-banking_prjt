package org.ebanking.dao;

import org.ebanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.owner.id = :clientId")
    List<Account> findByOwnerId(@Param("clientId") Long clientId);
}
