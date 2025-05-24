package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.TransactionStatus;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "idx_transaction_account", columnList = "account_id"),
        @Index(name = "idx_transaction_agent", columnList = "agent_id"),
        @Index(name = "idx_transaction_date", columnList = "transaction_date")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_transaction_reference", columnNames = {"reference"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private BankAgent agent;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 50)
    @NotNull
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @ColumnDefault("false")
    @Column(name = "is_verified")
    private Boolean verified = false;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @Column(name = "verification_comment", columnDefinition = "TEXT")
    private String verificationComment;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... other getters/setters
}

