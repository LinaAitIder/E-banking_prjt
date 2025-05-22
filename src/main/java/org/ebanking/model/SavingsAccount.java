package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "compte_epargne")
public class SavingsAccount {
    @Id
    @Column(name = "id_compte", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_compte", nullable = false)
    private Account account;

    @NotNull
    @Column(name = "taux_interet", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "date_prochain_interet")
    private LocalDate nextInterestDate;

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

    public BigDecimal getinterestRate() {
        return interestRate;
    }

    public void setinterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getnextInterestDate() {
        return nextInterestDate;
    }

    public void setnextInterestDate(LocalDate nextInterestDate) {
        this.nextInterestDate = nextInterestDate;
    }

}