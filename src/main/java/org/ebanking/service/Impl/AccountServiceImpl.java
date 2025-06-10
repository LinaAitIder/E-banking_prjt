package org.ebanking.service.Impl;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.*;
import org.ebanking.model.enums.AccountType;
import org.ebanking.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;


    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountNumber(account.getAccountNumber());
        response.setHolder(account.getOwner() != null ? account.getOwner().getFullName() : "Unknown");
        response.setBalance(account.getBalance());
        response.setAccountType(account.getType().name());
        response.setCurrency(account.getCurrency());
        response.setCreatedAt(account.getCreatedAt());
        response.setRib(account.getRib());
        response.setId(account.getId());

        if (account instanceof CurrentAccount) {
            response.setOverdraftLimit(((CurrentAccount) account).getOverdraftLimit());
        } else if (account instanceof SavingsAccount) {
            response.setInterestRate(((SavingsAccount) account).getInterestRate());
        } else if (account instanceof CryptoAccount) {
            response.setSupportedCryptos(((CryptoAccount) account).getSupportedCryptos());
        }

        return response;
    }


    @Override
    public AccountResponse createAccount(Long userId, AccountRequest request) {
        // 1. Récupérer le client
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + userId));

        // 2. Créer le compte selon le type
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
                throw new IllegalArgumentException("Invalid account type: " + request.getAccountType());
        }

        // 3. Configurer le compte
        account.setCurrency(request.getCurrency());
        account.setOwner(client);
        account.setAccountNumber(generateAccountNumber(request.getAccountType()));

        if ("CURRENT".equalsIgnoreCase(request.getAccountType()) || "SAVINGS".equalsIgnoreCase(request.getAccountType())) {
            account.setRib(RibGenerator.generateRib());
        }

        // 4. Sauvegarder
        Account savedAccount = accountRepository.save(account);
        client.addAccount(savedAccount);

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
        // 1. Verify account exists
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        // 2. Check account status
        if (!account.getActive()) {
            throw new IllegalStateException("Cannot delete inactive account");
        }

        // 3. Validate balance conditions
        if (account instanceof CurrentAccount) {
            CurrentAccount current = (CurrentAccount) account;
            if (current.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("Cannot delete current account with negative balance");
            }
        } else if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account balance must be zero for deletion");
        }

        // 4. Perform deletion
        try {
            accountRepository.delete(account);
            logger.info("Account {} successfully deleted", id);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to delete account {}: {}", id, e.getMessage());
            throw new TransactionSystemException("Deletion failed due to data integrity constraints");
        }
    }

    @Override
    public void deactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

        // Business rule checks
        if (!account.getActive()) {
            throw new IllegalStateException("Account is already deactivated");
        }

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("Account balance must be zero to deactivate");
        }

        // Soft delete (deactivation)
        account.setActive(false);
        accountRepository.save(account);

        logger.info("Account {} successfully deactivated", accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByOwnerId(Long clientId) {
        return accountRepository.findByOwnerId(clientId);
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
        return accountRepository.existsByOwnerId(clientId);
    }


    // verify if the Client has already account with the same type
    @Override
    public boolean clientCanHaveAccountType(Long clientId, Class<? extends Account> accountType) {
        if (CurrentAccount.class.equals(accountType)) {
            return accountRepository.findByOwnerId(clientId).isEmpty();
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

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountDetails(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        AccountResponse response = new AccountResponse();

        // Common fields
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getClass().getSimpleName().replace("Account", ""));
        response.setBalance(account.getBalance());
        response.setCurrency(account.getCurrency());
        response.setActive(account.getActive());
        response.setCreatedAt(account.getCreatedAt());
        response.setRib(account.getRib());
        response.setHolder(account.getOwner().getFullName());

        // Type-specific fields
        if (account instanceof CurrentAccount) {
            CurrentAccount current = (CurrentAccount) account;
            response.setOverdraftLimit(current.getOverdraftLimit());
            response.setBalance(current.getBalance());
        }
        else if (account instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) account;
            response.setInterestRate(savings.getInterestRate());
        }
        else if (account instanceof CryptoAccount) {
            CryptoAccount crypto = (CryptoAccount) account;
            response.setSupportedCryptos(crypto.getSupportedCryptos());
        }

        return response;
    }

    @Override
    public List<AccountResponse> getAccountsByClientAndType(Long clientId, AccountType type) {
        List<Account> accounts;

        if (type == null) {accounts = accountRepository.findByOwnerId(clientId);
        } else {
            accounts = accountRepository.findByOwnerIdAndType(clientId, type);
        }

        List<AccountResponse> result = new ArrayList<>();
        for (Account account : accounts) {
            result.add(mapToResponse(account));
        }

        return result;
    }


    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

}