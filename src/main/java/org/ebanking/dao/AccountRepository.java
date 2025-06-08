package org.ebanking.dao;

import org.ebanking.model.Account;
import org.ebanking.model.CurrentAccount;
import org.ebanking.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Méthodes JpaRepository :
    // - save()
    // - findById()
    // - existsById()
    // - delete()
    // - findAll() etc.


    List<Account> findByOwnerId(Long OwnerId);

    boolean existsByOwnerId(Long OwnerId);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM CurrentAccount a WHERE a.owner.id = :clientId")
    Optional<CurrentAccount> findCurrentAccountByOwnerId(@Param("clientId") Long clientId);

    Optional<CurrentAccount> findByAccountNumber(String accountNumber);

    List<Account> findByCurrency(String currency);

    List<Account>

    findByIsActiveTrue();

    List<Account> findByOwnerIdAndType(Long clientId, AccountType type);

}