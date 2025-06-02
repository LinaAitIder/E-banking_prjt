package org.ebanking.service;

import org.ebanking.dao.AccountRepository;
import org.ebanking.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Transactional
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

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
