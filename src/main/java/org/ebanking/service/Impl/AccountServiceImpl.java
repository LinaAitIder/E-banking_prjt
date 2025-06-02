package org.ebanking.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.*;
import org.ebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

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
    public void transfer(Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        Account source = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account target = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        if (!source.getCurrency().equals(target.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }

        BigDecimal availableBalance = getAvailableBalance(sourceAccountId);
        if (availableBalance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(target);
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

    @Override
    public void applyInterest(Long savingsAccountId) {
        Account account = accountRepository.findById(savingsAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!(account instanceof SavingsAccount)) {
            throw new IllegalStateException("Account is not a savings account");
        }

        SavingsAccount savings = (SavingsAccount) account;
        BigDecimal interest = savings.getBalance()
                .multiply(savings.getInterestRate())
                .divide(BigDecimal.valueOf(100));

        savings.setBalance(savings.getBalance().add(interest));
        accountRepository.save(savings);
    }

    @Override
    public void addSupportedCrypto(Long cryptoAccountId, String cryptoName, String walletAddress) {
        Account account = accountRepository.findById(cryptoAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!(account instanceof CryptoAccount)) {
            throw new IllegalStateException("Account is not a crypto account");
        }

        CryptoAccount crypto = (CryptoAccount) account;
        crypto.getSupportedCryptos().put(cryptoName, walletAddress);
        accountRepository.save(crypto);
    }

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
        return prefix + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
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
}