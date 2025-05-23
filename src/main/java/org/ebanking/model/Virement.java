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
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "virement")
public class Virement {
    @Id
    @Column(name = "id_transaction", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_transaction", nullable = false)
    private Transaction transaction;

    @Size(max = 30)
    @NotNull
    @Column(name = "compte_destination", nullable = false, length = 30)
    private String destinationAccount;

    @Column(name = "motif", length = Integer.MAX_VALUE)
    private String reason;

    @ColumnDefault("false")
    @Column(name = "est_instantane")
    private Boolean isInstantaneous;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getdestinationAccount() {
        return destinationAccount;
    }

    public void setdestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getreason() {
        return reason;
    }

    public void setreason(String reason) {
        this.reason = reason;
    }

    public Boolean getisInstantaneous() {
        return isInstantaneous;
    }

    public void setisInstantaneous(Boolean isInstantaneous) {
        this.isInstantaneous = isInstantaneous;
    }

}