package org.ebanking.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.*;
import org.ebanking.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public AccountResponse createAccount(AccountRequest request) {
        if (request.getClientId() == null) {
            throw new IllegalArgumentException("Client ID must not be null");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + request.getClientId()));
        Account account;
        switch (request.getAccountType()) {
            case "CURRENT":
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setOverdraftLimit(request.getOverdraftLimit());
                account = currentAccount;
                break;
            case "SAVINGS":
                SavingsAccount savingsAccount = new SavingsAccount();
                savingsAccount.setInterestRate(request.getInterestRate());
                account = savingsAccount;
                break;
            case "CRYPTO":
                CryptoAccount cryptoAccount = new CryptoAccount();
                cryptoAccount.setSupportedCryptos(request.getSupportedCryptos());
                account = cryptoAccount;
                break;
            default:
                throw new RuntimeException("Invalid account type");
        }

        account.setCurrency(request.getCurrency());
        account.setClient(client);
        account.setAccountNumber(generateAccountNumber(request.getAccountType()));

        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account updateAccount(Account account) {
        if (!accountRepository.existsById(account.getId())) {
            throw new IllegalArgumentException("Account not found");
        }
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account instanceof CurrentAccount) {
            CurrentAccount current = (CurrentAccount) account;
            if (current.getBalance().compareTo(BigDecimal.ZERO) < 0
                    && current.getBalance().abs().compareTo(current.getOverdraftLimit()) > 0) {
                throw new IllegalStateException("Cannot delete account with unauthorized overdraft");
            }
        } else if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account balance must be zero to delete");
        }

        accountRepository.delete(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account instanceof CurrentAccount) {
            CurrentAccount current = (CurrentAccount) account;
            return current.getBalance().add(current.getOverdraftLimit());
        }
        return account.getBalance();
    }


    // verify if a client has an account
    public boolean clientHasAccounts(Long clientId) {
        return accountRepository.existsByClientId(clientId);
    }


    // verify if the Client has already account with the same type
    @Override
    public boolean clientCanHaveAccountType(Long clientId, Class<? extends Account> accountType) {
        if (CurrentAccount.class.equals(accountType)) {
            return accountRepository.findByClientId(clientId).isEmpty();
        }
        return true;
    }

    private String generateAccountNumber(String accountType) {
        String prefix = accountType.equals("CURRENT") ? "CUR-" :
                accountType.equals("SAVINGS") ? "SAV-" : "CRYPTO-";
        return prefix + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }

    private AccountResponse convertToDto(Account account) {
        AccountResponse dto = new AccountResponse();
        // Copie des champs communs
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCurrency(account.getCurrency());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getClass().getSimpleName().toUpperCase().replace("ACCOUNT", ""));

        // Copie des champs spécifiques
        if (account instanceof CurrentAccount) {
            dto.setOverdraftLimit(((CurrentAccount) account).getOverdraftLimit());
        } else if (account instanceof SavingsAccount) {
            dto.setInterestRate(((SavingsAccount) account).getInterestRate());
        } else if (account instanceof CryptoAccount) {
            dto.setSupportedCryptos(((CryptoAccount) account).getSupportedCryptos());
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean doesAccountExist(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    public void creditAccount(Account account, BigDecimal amount) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Vérification supplémentaire
        if (account.getId() == null) {
            account = accountRepository.save(account);
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        // Audit log
        logger.info("Account {} credited with {}", account.getId(), amount);

    }


}