package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a cryptocurrency transaction with blockchain verification.
 */
@Entity
@Table(name = "crypto_transaction", uniqueConstraints = {
        @UniqueConstraint(name = "uk_crypto_transaction_hash", columnNames = {"blockchain_hash"})
})
public class CryptoTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CryptoWallet wallet;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "crypto_type", nullable = false, length = 10)
    private CryptoType cryptoType;

    @NotNull
    @Positive
    @Column(name = "quantity", nullable = false, precision = 24, scale = 8) // Supports 8 decimal places
    private BigDecimal quantity;

    @NotNull
    @PositiveOrZero
    @Column(name = "current_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 10)
    private OperationType operationType;

    @Size(max = 66) // Standard blockchain hash length
    @Column(name = "blockchain_hash", unique = true)
    private String blockchainHash;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate = Instant.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status = TransactionStatus.CONFIRMED;

    // Enums for type safety
    public enum CryptoType {
        BTC, ETH, XRP, LTC, BNB
    }

    public enum OperationType {
        BUY, SELL, TRANSFER, SWAP
    }

    public enum TransactionStatus {
        PENDING, CONFIRMED, FAILED, REVERTED
    }

    // Constructors
    public CryptoTransaction() {}

    public CryptoTransaction(CryptoWallet wallet, CryptoType cryptoType,
                             BigDecimal quantity, BigDecimal currentValue,
                             OperationType operationType) {
        this.wallet = wallet;
        this.cryptoType = cryptoType;
        this.quantity = quantity;
        this.currentValue = currentValue;
        this.operationType = operationType;
    }

    // Business Methods
    public BigDecimal calculateTotalValue() {
        return quantity.multiply(currentValue);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CryptoWallet getWallet() { return wallet; }
    public void setWallet(CryptoWallet wallet) { this.wallet = wallet; }

    public CryptoType getCryptoType() { return cryptoType; }
    public void setCryptoType(CryptoType cryptoType) { this.cryptoType = cryptoType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getCurrentValue() { return currentValue; }
    public void setCurrentValue(BigDecimal currentValue) { this.currentValue = currentValue; }

    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getBlockchainHash() { return blockchainHash; }
    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public Instant getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
}