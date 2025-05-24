package org.ebanking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interest-bearing savings account with specific rate management.
 * Inherits core account properties from Account base class.
 */
@Entity
@Table(name = "savings_account")
@PrimaryKeyJoinColumn(name = "account_id")  // Matches Account's ID column
@DiscriminatorValue("SAVINGS")             // Value for account_type discriminator
public class SavingsAccount extends Account {

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "next_interest_date")
    private LocalDate nextInterestDate;

    // Constructors
    public SavingsAccount() {
        // Default constructor for JPA
    }

    public SavingsAccount(String accountNumber, BigDecimal balance, String currency,
                          BigDecimal interestRate) {
        this.setAccountNumber(accountNumber);
        this.setBalance(balance);
        this.setCurrency(currency);
        this.interestRate = interestRate;
        this.nextInterestDate = LocalDate.now().plusMonths(1);
    }

    // Getters and Setters
    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getNextInterestDate() {
        return nextInterestDate;
    }

    public void setNextInterestDate(LocalDate nextInterestDate) {
        this.nextInterestDate = nextInterestDate;
    }

}