package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "utilisateur", uniqueConstraints = {
        @UniqueConstraint(name = "utilisateur_email_key", columnNames = {"email"})
})
public class User {
    @Id
    @ColumnDefault("nextval('utilisateur_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Size(max = 100)
    @NotNull
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Size(max = 20)
    @Column(name = "telephone", length = 20)
    private String telephone;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_creation")
    private Instant dateCreation;

    @ColumnDefault("true")
    @Column(name = "est_actif")
    private Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getisActive() {
        return isActive;
    }

    public void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

}