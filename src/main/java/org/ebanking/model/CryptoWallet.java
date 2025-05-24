package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents a cryptocurrency wallet linked to a banking account.
 * Stores wallet addresses and cryptocurrency balances.
 */
@Entity
@Table(name = "crypto_wallet",
        indexes = @Index(name = "idx_crypto_wallet_account", columnList = "account_id"),
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_crypto_wallet_account", columnNames = {"account_id"}),
                @UniqueConstraint(name = "uk_crypto_wallet_address", columnNames = {"wallet_address"})
        })
public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account linkedAccount;

    @NotBlank
    @Size(max = 255)
    @Column(name = "wallet_address", nullable = false)
    private String walletAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "crypto_balances", columnDefinition = "jsonb default '{}'")
    private Map<String, BigDecimal> cryptoBalances;

    @Column(name = "api_key_encrypted", length = 512)
    private String encryptedApiKey;

    @Size(max = 50)
    @Column(name = "exchange_name")
    private String exchangeName;

    @ColumnDefault("false")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    // Enums for supported exchanges
    public enum Exchange {
        BINANCE, COINBASE, KRAKEN, FTX
    }

    // Constructors
    public CryptoWallet() {}

    public CryptoWallet(Account linkedAccount, String walletAddress) {
        this.linkedAccount = linkedAccount;
        this.walletAddress = walletAddress;
    }

    // Business Methods
    public void addCryptoBalance(String symbol, BigDecimal amount) {
        this.cryptoBalances.merge(symbol, amount, BigDecimal::add);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Account getLinkedAccount() { return linkedAccount; }
    public void setLinkedAccount(Account linkedAccount) {
        this.linkedAccount = linkedAccount;
    }

    public String getWalletAddress() { return walletAddress; }
    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Map<String, BigDecimal> getCryptoBalances() { return cryptoBalances; }
    public void setCryptoBalances(Map<String, BigDecimal> cryptoBalances) {
        this.cryptoBalances = cryptoBalances;
    }

    public String getEncryptedApiKey() { return encryptedApiKey; }
    public void setEncryptedApiKey(String encryptedApiKey) {
        this.encryptedApiKey = encryptedApiKey;
    }

    public String getExchangeName() { return exchangeName; }
    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Boolean isActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }
}