package org.ebanking.service;

import org.ebanking.model.Account;
import org.ebanking.model.CryptoAccount;
import org.ebanking.model.CurrentAccount;
import org.ebanking.model.SavingsAccount;
import org.springframework.stereotype.Service;

@Service
public class AccountFactory {
    public Account createAccount(Account.AccountType type) {
        return switch (type) {
            case CURRENT -> new CurrentAccount();
            case SAVINGS -> new SavingsAccount();
            case CRYPTO -> new CryptoAccount();
            default -> throw new IllegalArgumentException("Unknown account type");
        };
    }
}
