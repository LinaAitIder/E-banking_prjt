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
import org.ebanking.model.BankAgent;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction", indexes = {
        @Index(name = "idx_transaction_compte", columnList = "id_compte"),
        @Index(name = "idx_transaction_agent", columnList = "id_agent"),
        @Index(name = "idx_transaction_date", columnList = "date_transaction")
}, uniqueConstraints = {
        @UniqueConstraint(name = "transaction_reference_key", columnNames = {"reference"})
})
public class Transaction {
    @Id
    @ColumnDefault("nextval('transaction_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_compte", nullable = false)
    private Account idAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_agent")
    private BankAgent idAgent;

    @NotNull
    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_transaction")
    private Instant transactionDate;

    @Size(max = 20)
    @ColumnDefault("'En attente'")
    @Column(name = "statut", length = 20)
    private String status;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 50)
    @NotNull
    @Column(name = "reference", nullable = false, length = 50)
    private String reference;

    @Size(max = 20)
    @NotNull
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @ColumnDefault("false")
    @Column(name = "est_verifie")
    private Boolean isVerified;

    @Column(name = "date_verification")
    private Instant verificationDate;

    @Column(name = "commentaire_verification", length = Integer.MAX_VALUE)
    private String verificationComment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getIdCompte() {
        return idAccount;
    }

    public void setIdCompte(Account idAccount) {
        this.idAccount = idAccount;
    }

    public BankAgent getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(BankAgent idAgent) {
        this.idAgent = idAgent;
    }

    public BigDecimal getamount() {
        return amount;
    }

    public void setamount(BigDecimal amount) {
        this.amount = amount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getisVerified() {
        return isVerified;
    }

    public void setisVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Instant getverificationDate() {
        return verificationDate;
    }

    public void setverificationDate(Instant verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getverificationComment() {
        return verificationComment;
    }

    public void setverificationComment(String verificationComment) {
        this.verificationComment = verificationComment;
    }

}