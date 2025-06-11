package org.ebanking.controller;

import jakarta.validation.Valid;
import org.ebanking.dto.request.TransferAmountRequest;
import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.CurrentAccount;
import org.ebanking.model.SavingsAccount;
import org.ebanking.model.enums.TransactionStatus;
import org.ebanking.service.AccountService;
import org.ebanking.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ebanking.model.Account;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;
    private final AccountService accountService;


    public TransferController(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(
            @Valid @RequestBody TransferRequest transferRequest,
            @RequestHeader("client-id") Long clientId) {

        TransferResponse response = transferService.processTransfer(clientId, transferRequest);

        return ResponseEntity.status(response.getStatus() == TransactionStatus.COMPLETED ?
                        HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/save-money")
    public ResponseEntity<TransferResponse> saveMoney(
            @Valid @RequestBody TransferAmountRequest amountRequest,
            @RequestHeader("client-id") Long clientId) {

        try {
            TransferResponse response = transferService.transferToSavings(clientId, amountRequest.getAmount());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new TransferResponse(null, null, null, null,
                            TransactionStatus.FAILED, null, e.getMessage()));
        }
    }

    @PostMapping("/withdraw-from-savings")
    public ResponseEntity<TransferResponse> getMoneyFromSavings(
            @Valid @RequestBody TransferRequest transferRequest,
            @RequestHeader("client-id") Long clientId) {

        SavingsAccount savingsAccount = accountService.findSavingsAccountByClientId(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No savings account found"));

        CurrentAccount currentAccount = accountService.findCurrentAccountByClientId(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No current account found"));

        TransferRequest reverseRequest = new TransferRequest();
        reverseRequest.setAmount(transferRequest.getAmount());
        reverseRequest.setDestinationAccount(currentAccount.getAccountNumber());

        TransferResponse response = transferService.processTransferFromSavings(
                clientId, savingsAccount.getAccountNumber(), reverseRequest);

        return ResponseEntity.ok(response);
    }
}


