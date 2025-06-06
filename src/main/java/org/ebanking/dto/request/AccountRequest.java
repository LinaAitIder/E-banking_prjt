package org.ebanking.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class AccountRequest {

    @NotNull
    private Long clientId;

    private String accountType;  // "CURRENT", "SAVINGS", etc.
    private String currency;
    private BigDecimal overdraftLimit;  // Pour CurrentAccount
    private BigDecimal interestRate;    // Pour SavingsAccount
    private Map<String, String> supportedCryptos; // Pour CryptoAccount


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

    public Map<String, String> getSupportedCryptos() {
        return supportedCryptos;
    }

    public void setSupportedCryptos(Map<String, String> supportedCryptos) {
        this.supportedCryptos = supportedCryptos;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}