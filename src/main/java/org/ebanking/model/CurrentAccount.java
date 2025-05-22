package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "compte_courant")
public class CurrentAccount {
    @Id
    @Column(name = "id_compte", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_compte", nullable = false)
    private Account account;

    @ColumnDefault("0.0")
    @Column(name = "plafond_decouvert", precision = 15, scale = 2)
    private BigDecimal overdraftLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getCompte() {
        return account;
    }

    public void setCompte(Account account) {
        this.account = account;
    }

    public BigDecimal getoverdraftLimit() {
        return overdraftLimit;
    }

    public void setoverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

}