package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a QR code payment transaction with expiration tracking.
 */
@Entity
@Table(name = "qr_payment")
public class QRPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Transaction transaction;

    @NotBlank
    @Lob
    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    @NotNull
    @Column(name = "expiration_time", nullable = false)
    private Instant expirationTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private QRStatus status = QRStatus.PENDING;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Size(max = 3)
    @Column(name = "currency", length = 3)
    private String currency;

    // QR Payment status
    public enum QRStatus {
        PENDING,
        SCANNED,
        COMPLETED,
        EXPIRED,
        FAILED
    }

    // Constructors
    public QRPayment() {}

    public QRPayment(String qrCode, Instant expirationTime, BigDecimal amount, String currency) {
        this.qrCode = qrCode;
        this.expirationTime = expirationTime;
        this.amount = amount;
        this.currency = currency;
    }

    // Business Methods
    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public Instant getExpirationTime() { return expirationTime; }
    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public QRStatus getStatus() { return status; }
    public void setStatus(QRStatus status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}