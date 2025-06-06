package org.ebanking.service.Impl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Override
    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {

        //List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionDate(),
                getTransactionType(transaction),
                transaction.getAmount()
        );
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
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }
}