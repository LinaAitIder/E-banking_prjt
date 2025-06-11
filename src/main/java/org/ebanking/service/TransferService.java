package org.ebanking.service;

import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.Transfer;

import java.math.BigDecimal;

public interface TransferService {


    public TransferResponse processTransfer(Long clientId, TransferRequest request);
    TransferResponse processTransferFromSavings(Long ownerId, String savingsAccountNumber, TransferRequest request);
    String findClientSavingsAccount(Long clientId);
    String findClientCurrentAccount(Long clientId);
    TransferResponse transferToSavings(Long clientId, BigDecimal amount);
}