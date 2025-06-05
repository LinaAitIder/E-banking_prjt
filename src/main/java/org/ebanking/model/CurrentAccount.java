package org.ebanking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Standard checking account with overdraft capability.
 * Inherits core account properties from Account base class.
 */
@Entity
@Table(name = "current_account")
@PrimaryKeyJoinColumn(name = "account_id")  // Matches Account's ID column
@DiscriminatorValue("CURRENT")             // Value for account_type discriminator
public class CurrentAccount extends Account {

    @Column(name = "overdraft_limit", precision = 15, scale = 2)
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    @Override
    public AccountType getType() {
        return AccountType.CURRENT;
    }

    // Constructors
    public CurrentAccount() {
        // Default constructor for JPA
    }

    public CurrentAccount(String accountNumber, BigDecimal balance,
                          String currency, BigDecimal overdraftLimit) {
        this.setAccountNumber(accountNumber);
        this.setBalance(balance);
        this.setCurrency(currency);
        this.overdraftLimit = overdraftLimit;
    }

    // Business Logic Methods
    public BigDecimal getAvailableBalance() {
        return getBalance().add(overdraftLimit);
    }

    // Getters and Setters
    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}