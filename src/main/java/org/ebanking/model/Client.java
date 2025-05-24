package org.ebanking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "national_id", unique = true)
    private String nationalId;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "terms_accepted")
    private Boolean termsAccepted;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;

    @Column(name = "web_authn_enabled", nullable = false)
    private boolean webAuthnEnabled = false;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<WebAuthnCredential> webAuthnCredentials = new HashSet<>();

    // Additional methods
    public void addWebAuthnCredential(WebAuthnCredential credential) {
        this.webAuthnCredentials.add(credential);
        credential.setClient(this);
    }

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    // Getters/setters
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    // ... other getters/setters

    public Boolean getTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public boolean isWebAuthnEnabled() {
        return webAuthnEnabled;
    }

    public void setWebAuthnEnabled(boolean webAuthnEnabled) {
        this.webAuthnEnabled = webAuthnEnabled;
    }

    public Set<WebAuthnCredential> getWebAuthnCredentials() {
        return webAuthnCredentials;
    }

    public void setWebAuthnCredentials(Set<WebAuthnCredential> webAuthnCredentials) {
        this.webAuthnCredentials = webAuthnCredentials;
    }
}