package org.ebanking.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "user_consent")
public class UserConsent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String consentType; // Ex: "MARKETING", "COOKIES", "DATA_PROCESSING"

    @Column(nullable = false, unique = true)
    private String reference; // "CONSENT-2023-XYZ123"

    @Column(nullable = false)
    private Instant givenAt;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private Instant expiresAt; // Date d'expiration (on peut choisir 1 an apres)

    // Constructeurs
    public UserConsent() {}

    public UserConsent(User user, String consentType, String reference, Instant expiresAt) {
        this.user = user;
        this.consentType = consentType;
        this.reference = reference;
        this.givenAt = Instant.now();
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConsentType() {
        return consentType;
    }

    public void setConsentType(String consentType) {
        this.consentType = consentType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getGivenAt() {
        return givenAt;
    }

    public void setGivenAt(Instant givenAt) {
        this.givenAt = givenAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}



