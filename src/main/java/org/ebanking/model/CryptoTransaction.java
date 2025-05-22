package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "transaction_crypto", uniqueConstraints = {
        @UniqueConstraint(name = "transaction_crypto_hash_key", columnNames = {"hash"})
})
public class CryptoTransaction {
    @Id
    @ColumnDefault("nextval('transaction_crypto_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_portefeuille", nullable = false)
    private CryptoWallet walletId;

    @Size(max = 10)
    @NotNull
    @Column(name = "type_crypto", nullable = false, length = 10)
    private String cryptoType;

    @NotNull
    @Column(name = "quantite", nullable = false, precision = 18, scale = 8)
    private BigDecimal quantity;

    @NotNull
    @Column(name = "valeur_moment", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue;

    @Size(max = 10)
    @NotNull
    @Column(name = "type_operation", nullable = false, length = 10)
    private String operationType;

    @Size(max = 255)
    @Column(name = "hash")
    private String hash;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_transaction")
    private Instant transactionDate;

    @Size(max = 20)
    @ColumnDefault("'Confirmée'")
    @Column(name = "statut", length = 20)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CryptoWallet getwalletId() {
        return walletId;
    }

    public void setwalletId(CryptoWallet walletId) {
        this.walletId = walletId;
    }

    public String getcryptoType() {
        return cryptoType;
    }

    public void setcryptoType(String cryptoType) {
        this.cryptoType = cryptoType;
    }

    public BigDecimal getquantity() {
        return quantity;
    }

    public void setquantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getcurrentValue() {
        return currentValue;
    }

    public void setcurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public String getoperationType() {
        return operationType;
    }

    public void setoperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Instant gettransactionDate() {
        return transactionDate;
    }

    public void settransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

}