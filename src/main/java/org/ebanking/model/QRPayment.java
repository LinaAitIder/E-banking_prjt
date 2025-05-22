package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "paiement_qr")
public class QRPayment {
    @Id
    @ColumnDefault("nextval('paiement_qr_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_transaction")
    private org.ebanking.model.Transaction idTransaction;

    @NotNull
    @Column(name = "code_qr", nullable = false, length = Integer.MAX_VALUE)
    private String codeQr;

    @NotNull
    @Column(name = "date_expiration", nullable = false)
    private Instant expirationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public org.ebanking.model.Transaction getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(org.ebanking.model.Transaction idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getCodeQr() {
        return codeQr;
    }

    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    public Instant getexpirationDate() {
        return expirationDate;
    }

    public void setexpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

}