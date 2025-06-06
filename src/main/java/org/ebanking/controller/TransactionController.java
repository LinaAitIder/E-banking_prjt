package org.ebanking.controller;

import org.ebanking.dto.response.TransactionResponse;
import org.ebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/by-account")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(
            @RequestHeader("account-id") Long accountId) {

        List<TransactionResponse> responses = transactionService.getTransactionsByAccount(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TransactionResponse>> getRecentTransactions(
            @RequestHeader("account-id") Long accountId) {

        List<TransactionResponse> responses = transactionService.getRecentTransactions(accountId);
        return ResponseEntity.ok(responses);
    }

}