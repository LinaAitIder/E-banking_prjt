package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.math.BigDecimal;

/**
 * Represents a budget category with allocated spending limits and tracking.
 */
@Entity
@Table(name = "budget_category")
public class BudgetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Budget budget;

    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotNull
    @PositiveOrZero
    @Column(name = "allocated_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal allocatedAmount;

    @ColumnDefault("0.00")
    @PositiveOrZero
    @Column(name = "spent_amount", precision = 19, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @ColumnDefault("'#CCCCCC'")
    @Column(name = "color", length = 7)
    private String color = "#CCCCCC";

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", length = 20)
    private CategoryType type;

    // Budget category types
    public enum CategoryType {
        FOOD,
        TRANSPORTATION,
        HOUSING,
        ENTERTAINMENT,
        UTILITIES,
        SAVINGS
    }

    // Constructors
    public BudgetCategory() {}

    public BudgetCategory(Budget budget, String name,
                          BigDecimal allocatedAmount, CategoryType type) {
        this.budget = budget;
        this.name = name;
        this.allocatedAmount = allocatedAmount;
        this.type = type;
    }

    // Business Methods
    public BigDecimal getRemainingAmount() {
        return allocatedAmount.subtract(spentAmount);
    }

    public boolean isOverBudget() {
        return spentAmount.compareTo(allocatedAmount) > 0;
    }

    public void addExpense(BigDecimal amount) {
        this.spentAmount = this.spentAmount.add(amount);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public CategoryType getType() { return type; }
    public void setType(CategoryType type) { this.type = type; }
}