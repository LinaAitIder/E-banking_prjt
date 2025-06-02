package org.ebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client owner;

    @Size(max = 30)
    @Column(name = "account_number", unique = true)
    private String accountNumber = generateAccountNumber();

    // Méthode pour génerer un numero de compte unique
    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", insertable = false, updatable = false)
    private AccountType type;

    public enum AccountType {
        CURRENT,
        SAVINGS,
        CRYPTO
    }

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Size(max = 3)
    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "is_active")
    private Boolean isActive = false;

    public Account() {}

    public Account(Client owner, String accountNumber, BigDecimal balance,
                   String currency) {
        this.balance = balance;
        this.owner = owner;
        this.currency = currency;
        this.accountNumber = accountNumber;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getOwner() { return owner; }

    public void setOwner(Client owner) {this.owner = owner; }

    public abstract AccountType getType();

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

