package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "categorie_budget")
public class BudgetCategory {
    @Id
    @ColumnDefault("nextval('categorie_budget_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_budget", nullable = false)
    private Budget idBudget;

    @Size(max = 50)
    @NotNull
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @NotNull
    @Column(name = "montant_alloue", nullable = false, precision = 15, scale = 2)
    private BigDecimal allocatedAmount;

    @ColumnDefault("0.0")
    @Column(name = "montant_depense", precision = 15, scale = 2)
    private BigDecimal spentAmount;

    @Size(max = 7)
    @ColumnDefault("'#000000'")
    @Column(name = "couleur", length = 7)
    private String color;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Budget getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(Budget idBudget) {
        this.idBudget = idBudget;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getallocatedAmount() {
        return allocatedAmount;
    }

    public void setallocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getspentAmount() {
        return spentAmount;
    }

    public void setspentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

}