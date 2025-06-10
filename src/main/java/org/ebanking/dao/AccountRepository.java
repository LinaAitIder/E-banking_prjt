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

    @Query("SELECT a FROM Account a WHERE a.owner IS NOT NULL")
    List<Account> findAllWithOwner();

    boolean existsByOwnerId(Long OwnerId);

    boolean existsByAccountNumber(String accountNumber);


    @Query("SELECT a FROM CurrentAccount a WHERE a.owner.id = :ownerId")
    Optional<CurrentAccount> findCurrentAccountByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT a.owner.fullName FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<String> findOwnerNameByAccountNumber(@Param("accountNumber") String accountNumber);

    Optional<CurrentAccount> findByAccountNumber(String accountNumber);

    List<Account> findByCurrency(String currency);

    List<Account>

    findByIsActiveTrue();

    List<Account> findByOwnerIdAndType(Long clientId, AccountType type);

}