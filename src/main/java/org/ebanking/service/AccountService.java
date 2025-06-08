package org.ebanking.service;

import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.Account;
import org.ebanking.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    public AccountResponse createAccount(Long userId, AccountRequest request);

    Optional<Account> getAccountById(Long id);

    Account updateAccount(Account account);

    void deleteAccount(Long id);

    void deactivateAccount(Long accountId);

    List<Account> getAccountsByOwnerId(Long OwnerId);

    BigDecimal getAvailableBalance(Long accountId);

    boolean clientCanHaveAccountType(Long OwnerId, Class<? extends Account> accountType);

    boolean doesAccountExist(String accountNumber);

    void creditAccount(Account account, BigDecimal amount);

    boolean clientHasAccounts(Long clientId);

    public AccountResponse getAccountDetails(Long accountId);

    List<AccountResponse> getAccountsByClientAndType(Long clientId, AccountType type);

}