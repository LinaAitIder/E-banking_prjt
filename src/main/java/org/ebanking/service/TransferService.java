package org.ebanking.service;

import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.Transfer;

public interface TransferService {


    public TransferResponse processTransfer(Long clientId, TransferRequest request);
}