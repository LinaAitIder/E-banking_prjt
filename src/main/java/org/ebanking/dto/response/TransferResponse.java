package org.ebanking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.ebanking.model.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransferResponse {
    private Long transactionId;
    private String reference;
    private BigDecimal amount;
    private String destinationAccount;
    private TransactionStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX ", timezone = "UTC")
    private OffsetDateTime transactionDate;

    private String message;

    // Constructeurs
    public TransferResponse() {}

    public TransferResponse(Long transactionId, String reference, BigDecimal amount,
                               String destinationAccount, TransactionStatus status,
                               OffsetDateTime transactionDate, String message) {
        this.transactionId = transactionId;
        this.reference = reference;
        this.amount = amount;
        this.destinationAccount = destinationAccount;
        this.status = status;
        this.transactionDate = transactionDate;
        this.message = message;
    }


    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}