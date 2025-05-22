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
import org.ebanking.model.Admin;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "parametre", uniqueConstraints = {
        @UniqueConstraint(name = "parametre_cle_key", columnNames = {"cle"})
})
public class Parameter {
    @Id
    @ColumnDefault("nextval('parametre_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_admin")
    private Admin AdminId;

    @Size(max = 50)
    @NotNull
    @Column(name = "cle", nullable = false, length = 50)
    private String cle;

    @NotNull
    @Column(name = "valeur", nullable = false, length = Integer.MAX_VALUE)
    private String value;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("false")
    @Column(name = "est_systeme")
    private Boolean isSystem;

    @ColumnDefault("false")
    @Column(name = "est_devise")
    private Boolean isCurrency;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "derniere_modification")
    private Instant lastModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Admin getAdminId() {
        return AdminId;
    }

    public void setAdminId(Admin AdminId) {
        this.AdminId = AdminId;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getvalue() {
        return value;
    }

    public void setvalue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getisSystem() {
        return isSystem;
    }

    public void setisSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean getisCurrency() {
        return isCurrency;
    }

    public void setisCurrency(Boolean isCurrency) {
        this.isCurrency = isCurrency;
    }

    public Instant getlastModified() {
        return lastModified;
    }

    public void setlastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

}