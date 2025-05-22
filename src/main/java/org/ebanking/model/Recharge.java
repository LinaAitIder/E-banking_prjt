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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "recharge")
public class Recharge {
    @Id
    @Column(name = "id_transaction", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_transaction", nullable = false)
    private org.ebanking.model.Transaction transaction;

    @Size(max = 50)
    @NotNull
    @Column(name = "type_service", nullable = false, length = 50)
    private String typeService;

    @Size(max = 50)
    @NotNull
    @Column(name = "numero_recharge", nullable = false, length = 50)
    private String rechargeNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "operateur", nullable = false, length = 50)
    private String operateur;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public org.ebanking.model.Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(org.ebanking.model.Transaction transaction) {
        this.transaction = transaction;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public String getrechargeNumber() {
        return rechargeNumber;
    }

    public void setrechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

}