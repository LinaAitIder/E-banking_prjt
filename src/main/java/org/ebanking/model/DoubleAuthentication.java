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
@Table(name = "authentification_double")
public class DoubleAuthentication {
    @Id
    @ColumnDefault("nextval('authentification_double_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private User idUser;

    @Size(max = 50)
    @NotNull
    @Column(name = "methode", nullable = false, length = 50)
    private String methode;

    @Size(max = 8)
    @Column(name = "code_temporaire", length = 8)
    private String temporaryCode;

    @Column(name = "date_expiration")
    private Instant expirationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getIdUtilisateur() {
        return idUser;
    }

    public void setIdUtilisateur(User idUser) {
        this.idUser = idUser;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String gettemporaryCode() {
        return temporaryCode;
    }

    public void settemporaryCode(String temporaryCode) {
        this.temporaryCode = temporaryCode;
    }

    public Instant getexpirationDate() {
        return expirationDate;
    }

    public void setexpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

}