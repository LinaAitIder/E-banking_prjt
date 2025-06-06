package org.ebanking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionResponse {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime transactionDate;

    private String type; // TRANSFER, DEPOSIT, etc.
    private BigDecimal amount;

    // Constructeurs
    public TransactionResponse() {}

    public TransactionResponse(Long id, OffsetDateTime transactionDate,
                               String type, BigDecimal amount) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.type = type;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}