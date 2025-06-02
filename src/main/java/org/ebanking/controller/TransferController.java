package org.ebanking.controller;

import jakarta.validation.Valid;
import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.request.TransferRequest;
import org.ebanking.dto.response.TransferResponse;
import org.ebanking.model.enums.TransactionStatus;
import org.ebanking.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(
            @Valid @RequestBody TransferRequest transferRequest,
            @RequestHeader("X-Client-ID") Long clientId) {

        TransferResponse response = transferService.processTransfer(clientId, transferRequest);

        return ResponseEntity.status(response.getStatus() == TransactionStatus.COMPLETED ?
                        HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }

}