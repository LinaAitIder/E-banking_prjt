package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Cryptocurrency account extending base Account functionality.
 * Stores wallet addresses and supported cryptocurrencies.
 */
@Entity
@Table(name = "crypto_account")
@PrimaryKeyJoinColumn(name = "account_id")  // Matches Account's ID column
@DiscriminatorValue("CRYPTO")              // Value for account_type discriminator
public class CryptoAccount extends Account {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supported_cryptos", columnDefinition = "jsonb")
    private Map<String, Object> supportedCryptos;

    @Size(max = 255)
    @Column(name = "wallet_address", unique = true)
    private String walletAddress;

    @Override
    public AccountType getType() {
        return AccountType.CRYPTO;
    }

    // Constructors
    public CryptoAccount() {
        // Default constructor for JPA
    }

    public CryptoAccount(String accountNumber, String currency,
                         String walletAddress, Map<String, Object> supportedCryptos) {
        this.setAccountNumber(accountNumber);
        this.setCurrency(currency);
        this.setBalance(BigDecimal.ZERO);  // Crypto accounts typically start with 0 balance
        this.walletAddress = walletAddress;
        this.supportedCryptos = supportedCryptos;
    }

    // Getters and Setters
    public Map<String, Object> getSupportedCryptos() {
        return supportedCryptos;
    }

    public void setSupportedCryptos(Map<String, Object> supportedCryptos) {
        this.supportedCryptos = supportedCryptos;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}