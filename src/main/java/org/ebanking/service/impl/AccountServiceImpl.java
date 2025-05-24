package org.ebanking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ebanking.model.Account;
import org.ebanking.dao.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl {

    private final AccountRepository accountRepository;
    private final SecurityServiceImpl securityService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              SecurityServiceImpl securityService) {
        this.accountRepository = accountRepository;
        this.securityService = securityService;
    }

    public List<Account> getAccounts(Long clientId) {
        return accountRepository.findByClient_Id(clientId);
    }

    public Account createAccount(Account account) {
        securityService.validateAccountBalance(account, BigDecimal.ZERO);
        Account savedAccount = accountRepository.save(account);
        AuditLogLogLogService.logOperation(new AuditLogLogLogLog(
                "ACCOUNT_CREATION",
                "Création du compte " + savedAccount.getId(),
                savedAccount.getId()
        ));
        return savedAccount;
    }

    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Compte non trouvé"));
        if (!account.getTransactions().isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer un compte avec des transactions");
        }
        accountRepository.delete(account);
        AuditLogLogLogService.logOperation(new AuditLogLogLogLog(
                "ACCOUNT_DELETION",
                "Suppression du compte " + account.getId(),
                account.getId()
        ));
    }
}