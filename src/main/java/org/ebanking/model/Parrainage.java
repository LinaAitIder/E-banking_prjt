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
import org.ebanking.model.Client;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "parrainage", indexes = {
        @Index(name = "idx_parrainage_parrain", columnList = "id_parrain"),
        @Index(name = "idx_parrainage_filleul", columnList = "id_filleul")
}, uniqueConstraints = {
        @UniqueConstraint(name = "unique_parrainage", columnNames = {"id_parrain", "id_filleul"}),
        @UniqueConstraint(name = "parrainage_code_parrainage_key", columnNames = {"code_parrainage"})
})
public class Parrainage {
    @Id
    @ColumnDefault("nextval('parrainage_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_parrain", nullable = false)
    private Client idParrain;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_filleul", nullable = false)
    private Client idFilleul;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_parrainage")
    private Instant dateParrainage;

    @ColumnDefault("true")
    @Column(name = "est_active")
    private Boolean isActive;

    @ColumnDefault("0.0")
    @Column(name = "bonus_parrain", precision = 15, scale = 2)
    private BigDecimal bonusParrain;

    @ColumnDefault("0.0")
    @Column(name = "bonus_filleul", precision = 15, scale = 2)
    private BigDecimal bonusFilleul;

    @Size(max = 20)
    @NotNull
    @Column(name = "code_parrainage", nullable = false, length = 20)
    private String codeParrainage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getIdParrain() {
        return idParrain;
    }

    public void setIdParrain(Client idParrain) {
        this.idParrain = idParrain;
    }

    public Client getIdFilleul() {
        return idFilleul;
    }

    public void setIdFilleul(Client idFilleul) {
        this.idFilleul = idFilleul;
    }

    public Instant getDateParrainage() {
        return dateParrainage;
    }

    public void setDateParrainage(Instant dateParrainage) {
        this.dateParrainage = dateParrainage;
    }

    public Boolean getisActive() {
        return isActive;
    }

    public void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public BigDecimal getBonusParrain() {
        return bonusParrain;
    }

    public void setBonusParrain(BigDecimal bonusParrain) {
        this.bonusParrain = bonusParrain;
    }

    public BigDecimal getBonusFilleul() {
        return bonusFilleul;
    }

    public void setBonusFilleul(BigDecimal bonusFilleul) {
        this.bonusFilleul = bonusFilleul;
    }

    public String getCodeParrainage() {
        return codeParrainage;
    }

    public void setCodeParrainage(String codeParrainage) {
        this.codeParrainage = codeParrainage;
    }

}