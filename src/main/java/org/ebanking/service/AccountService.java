package org.ebanking.service;

import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    Optional<Account> getAccountById(Long id);

    Account updateAccount(Account account);

    void deleteAccount(Long id);

    List<Account> getAccountsByClientId(Long clientId);

    BigDecimal getAvailableBalance(Long accountId);

    boolean clientCanHaveAccountType(Long clientId, Class<? extends Account> accountType);

    boolean doesAccountExist(String accountNumber);

    void creditAccount(Account account, BigDecimal amount);
    boolean clientHasAccounts(Long clientId);
}