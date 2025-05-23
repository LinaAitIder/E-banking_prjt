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

import java.time.Instant;

@Entity
@Table(name = "paiement_biometrique")
public class BiometricPayment {
    @Id
    @ColumnDefault("nextval('paiement_biometrique_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_transaction")
    private org.ebanking.model.Transaction idTransaction;

    @Size(max = 20)
    @NotNull
    @Column(name = "type_biometrie", nullable = false, length = 20)
    private String biometricType;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "derniere_utilisation")
    private Instant lastUsedDate;

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

    public String getbiometricType() {
        return biometricType;
    }

    public void setbiometricType(String biometricType) {
        this.biometricType = biometricType;
    }

    public Instant getlastUsedDate() {
        return lastUsedDate;
    }

    public void setlastUsedDate(Instant lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }

}