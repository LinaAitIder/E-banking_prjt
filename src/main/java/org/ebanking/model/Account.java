package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "compte", indexes = {
        @Index(name = "idx_compte_client", columnList = "id_client")
}, uniqueConstraints = {
        @UniqueConstraint(name = "compte_numero_compte_key", columnNames = {"numero_compte"})
})
public class Account {
    @Id
    @ColumnDefault("nextval('compte_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "numero_compte", nullable = false, length = 30)
    private String accountNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ColumnDefault("0.0")
    @Column(name = "solde", precision = 15, scale = 2)
    private BigDecimal balance;

    @Size(max = 3)
    @NotNull
    @Column(name = "devise", nullable = false, length = 3)
    private String currency;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_creation")
    private Instant creationDate;

    @ColumnDefault("true")
    @Column(name = "est_actif")
    private Boolean isActive;

    @Size(max = 20)
    @NotNull
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getaccountNumber() {
        return accountNumber;
    }

    public void setaccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Client getclient() {
        return client;
    }

    public void setclient(Client client) {
        this.client = client;
    }

    public BigDecimal getbalance() {
        return balance;
    }

    public void setbalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getcurrency() {
        return currency;
    }

    public void setcurrency(String currency) {
        this.currency = currency;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getisActive() {
        return isActive;
    }

    public void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}