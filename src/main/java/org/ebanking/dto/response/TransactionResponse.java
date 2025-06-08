package org.ebanking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionResponse {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime transactionDate;
    private String type;
    private BigDecimal amount;
    private String sourceUser;       // Nom de l'utilisateur source
    private String destinationUser; // Nom de l'utilisateur destinataire
    private String sourceAccount;   // Numéro de compte source
    private String destinationAccount; // Numéro de compte destination

    // Constructeurs
    public TransactionResponse() {}


    public TransactionResponse(Long id, OffsetDateTime transactionDate, String type,
                               BigDecimal amount, String sourceUser, String destinationUser,
                               String sourceAccount, String destinationAccount) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.type = type;
        this.amount = amount;
        this.sourceUser = sourceUser;
        this.destinationUser = destinationUser;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public OffsetDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(OffsetDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getDestinationUser() {
        return destinationUser;
    }

    public void setDestinationUser(String destinationUser) {
        this.destinationUser = destinationUser;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

}

