package org.ebanking.service;

import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountResponse createAccount(AccountRequest request);

    public boolean clientHasAccounts(Long clientId);

    Optional<Account> getAccountById(Long id);

    Account updateAccount(Account account);

    void deleteAccount(Long id);

    List<Account> getAccountsByClientId(Long clientId);

    void transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount);

    BigDecimal getAvailableBalance(Long accountId);

    boolean doesAccountExist(String accountNumber);

    void applyInterest(Long savingsAccountId);

    void addSupportedCrypto(Long cryptoAccountId, String cryptoName, String walletAddress);

    boolean clientCanHaveAccountType(Long clientId, Class<? extends Account> accountType);
}