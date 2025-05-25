package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "utilisateur", uniqueConstraints = {
        @UniqueConstraint(name = "utilisateur_email_key", columnNames = {"email"})
})
public abstract class User {
    @Id
    @ColumnDefault("nextval('utilisateur_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

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

    @Column(nullable = false)
    private boolean webAuthnEnabled = false;

    @Transient // Non persisté en base
    private transient String challenge; // Stocké temporairement pour WebAuthn

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<WebAuthnCredential> webAuthnCredentials = new HashSet<>();

    public void addWebAuthnCredential(WebAuthnCredential credential) {
        this.webAuthnCredentials.add(credential);
        credential.setUser(this);
    }
    public WebAuthnCredential getFirstWebAuthnCredential() {
        if (webAuthnCredentials == null || webAuthnCredentials.isEmpty()) {
            return null;
        }
        return webAuthnCredentials.iterator().next();}

    public abstract List<String> getRoles();

    public String getRole() {
        if (this instanceof Client) return "CLIENT";
        if (this instanceof BankAgent) return "AGENT";
        if (this instanceof Admin) return "ADMIN";
        return "USER";}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isWebAuthnEnabled() { return webAuthnEnabled; }

    public void setWebAuthnEnabled(boolean webAuthnEnabled) {
        this.webAuthnEnabled = webAuthnEnabled;
    }

    public String getChallenge() { return challenge; }

    public void setChallenge(String challenge) { this.challenge = challenge; }

    public Set<WebAuthnCredential> getWebAuthnCredentials() {
        return webAuthnCredentials;
    }

    public void setWebAuthnCredentials(Set<WebAuthnCredential> webAuthnCredentials) {
        this.webAuthnCredentials = webAuthnCredentials;
    }
}