package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a client's budgeting plan with defined time period and spending limits.
 */
@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "current_spending", precision = 19, scale = 2)
    private BigDecimal currentSpending = BigDecimal.ZERO;

    // Constructors
    public Budget() {}

    public Budget(Client client, BigDecimal totalAmount,
                  LocalDate startDate, LocalDate endDate) {
        this.client = client;
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Business Methods
    public BigDecimal getRemainingBudget() {
        return totalAmount.subtract(currentSpending);
    }

    public boolean isExceeded() {
        return currentSpending.compareTo(totalAmount) > 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCurrentSpending() { return currentSpending; }
    public void setCurrentSpending(BigDecimal currentSpending) {
        this.currentSpending = currentSpending;
    }
}