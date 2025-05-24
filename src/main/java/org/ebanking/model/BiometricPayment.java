package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.BiometricType;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents a biometric payment authorization record.
 * Links biometric authentication to financial transactions.
 */
@Entity
@Table(name = "biometric_payment")
public class BiometricPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Transaction transaction;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "biometric_type", nullable = false, length = 20)
    private BiometricType biometricType;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_used_date")
    private Instant lastUsedDate = Instant.now();

    // Constructors
    public BiometricPayment() {}

    public BiometricPayment(Transaction transaction, BiometricType biometricType) {
        this.transaction = transaction;
        this.biometricType = biometricType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }

    public BiometricType getBiometricType() { return biometricType; }
    public void setBiometricType(BiometricType biometricType) {
        this.biometricType = biometricType;
    }

    public Instant getLastUsedDate() { return lastUsedDate; }
    public void setLastUsedDate(Instant lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }
}