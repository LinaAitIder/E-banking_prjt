package org.ebanking.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ebanking.model.Account;
import org.ebanking.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransactionServiceImpl {

    private final TransactionRepository transactionRepository;
    private final AccountServiceImpl accountService;
    private final SecurityServiceImpl securityService;
    private final AuditLogLogLogService AuditLogLogLogService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountServiceImpl accountService,
                                  SecurityServiceImpl securityService,
                                  AuditLogLogLogService AuditLogLogLogService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.securityService = securityService;
        this.AuditLogLogLogService = AuditLogLogLogService;
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Account sourceAccount = accountService.getAccountDetails(transactionDTO.getSourceAccountId());
        Account destinationAccount = accountService.getAccountDetails(transactionDTO.getDestinationAccountId());

        securityService.validateTransaction(sourceAccount, transactionDTO.getAmount());

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setType(transactionDTO.getType());

        Transaction savedTransaction = transactionRepository.save(transaction);
        AuditLogLogLogService.logOperation(new AuditLogLogLogLog(
                "TRANSACTION_CREATION",
                "Création de la transaction " + savedTransaction.getId(),
                savedTransaction.getId()
        ));

        return savedTransaction;
    }

    public List<Transaction> getTransactions(Long accountId) {
        return transactionRepository.findBySourceAccount_IdOrDestinationAccount_Id(
                accountId, accountId);
    }

    public Transaction verifyTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction non trouvée"));
        transaction.setVerified(true);
        return transactionRepository.save(transaction);
    }
}
