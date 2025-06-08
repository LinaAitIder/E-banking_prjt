package org.ebanking.service.Impl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.TransactionRepository;
import org.ebanking.dto.response.TransactionResponse;
import org.ebanking.model.Recharge;
import org.ebanking.model.Transaction;
import org.ebanking.model.Transfer;
import org.ebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setTransactionDate(transaction.getTransactionDate());
        response.setType(transaction.getClass().getSimpleName());
        response.setAmount(transaction.getAmount());

        // Source account info
        if (transaction.getAccount() != null) {
            response.setSourceAccount(transaction.getAccount().getAccountNumber());
            if (transaction.getAccount().getOwner() != null) {
                response.setSourceUser(transaction.getAccount().getOwner().getFullName());
            }
        }

        // Destination info (for transfers)
        if (transaction instanceof Transfer) {
            Transfer transfer = (Transfer) transaction;
            response.setDestinationAccount(transfer.getDestinationAccount());

            // Get destination owner name
            String destOwnerName = accountRepository
                    .findOwnerNameByAccountNumber(transfer.getDestinationAccount())
                    .orElse("Inconnu");
            response.setDestinationUser(destOwnerName);
        }

        return response;
    }

    private String getTransactionType(Transaction transaction) {

        if (transaction instanceof Transfer) {
            return "TRANSFER";
        } else if (transaction instanceof Recharge) {
            return "RECHARGE";
        }
        return "CHRYPTO";
    }

    @Override
    public List<TransactionResponse> getRecentTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findRecentTransactions(accountId);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }
}