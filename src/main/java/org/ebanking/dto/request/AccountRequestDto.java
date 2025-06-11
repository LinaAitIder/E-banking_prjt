package org.ebanking.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AccountRequestDto {
    private Long id;
    private Long clientId;
    private String clientName;
    private String accountType;  // "CURRENT", "SAVINGS", "CRYPTO"
    private String currency;
    private LocalDateTime requestDate = LocalDateTime.now();
    private BigDecimal overdraftLimit;  // Pour CurrentAccount
    private BigDecimal interestRate;// Pour SavingsAccount

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> supportedCryptos = new HashMap<>(); // Pour CryptoAccount

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Map<String, String> getSupportedCryptos() {return supportedCryptos;}

    public void setSupportedCryptos(Map<String, String> supportedCryptos) {
        if (supportedCryptos != null) {
            this.supportedCryptos = supportedCryptos;
        }
    }
}