package org.ebanking.service;

import org.ebanking.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> getTransactionsByAccount(Long accountID);
    List<TransactionResponse> getRecentTransactions(Long accountId);
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getAllClientTransactions(Long clientId);
}