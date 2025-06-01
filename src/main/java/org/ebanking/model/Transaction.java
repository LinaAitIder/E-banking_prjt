package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.TransactionStatus;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

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

    @OneToMany(mappedBy = "transaction")
    private Set<BiometricPayment> biometricPayments;

    @OneToMany(mappedBy = "transaction")
    private Set<QRPayment> qrPayments;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... other getters/setters

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BankAgent getAgent() {
        return agent;
    }

    public void setAgent(BankAgent agent) {
        this.agent = agent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Instant getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Instant verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationComment() {
        return verificationComment;
    }

    public void setVerificationComment(String verificationComment) {
        this.verificationComment = verificationComment;
    }

    public Set<BiometricPayment> getBiometricPayments() {
        return biometricPayments;
    }

    public void setBiometricPayments(Set<BiometricPayment> biometricPayments) {
        this.biometricPayments = biometricPayments;
    }

    public Set<QRPayment> getQrPayments() {
        return qrPayments;
    }

    public void setQrPayments(Set<QRPayment> qrPayments) {
        this.qrPayments = qrPayments;
    }


}

