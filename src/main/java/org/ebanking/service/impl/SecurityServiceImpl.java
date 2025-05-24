package org.ebanking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ebanking.dao.ClientRepository;
import org.ebanking.model.Account;
import org.ebanking.model.Transaction;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public class SecurityServiceImpl {
    @Service
    @Transactional
    public class SecurityService {

        private final ClientRepository clientRepository;
        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

        @Autowired
        public SecurityService(ClientRepository clientRepository,
                               AccountRepository accountRepository,
                               TransactionRepository transactionRepository) {
            this.clientRepository = clientRepository;
            this.accountRepository = accountRepository;
            this.transactionRepository = transactionRepository;
        }

        public boolean verifyTransaction(Transaction transaction) {
            Account sourceAccount = accountRepository.findById(transaction.getSourceAccount().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Compte source non trouvé"));

            return validateAccountBalance(sourceAccount, transaction.getAmount());
        }

        public boolean validateAccountBalance(Account account, BigDecimal amount) {
            return account.getBalance().compareTo(amount) >= 0;
        }


    }

}
