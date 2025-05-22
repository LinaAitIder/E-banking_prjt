package org.ebanking.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    private String fullName;
    private Date dateOfBirth;
    private String nationalId;
    private String email;
    private String password;

    @Column(name = "phone_number", unique = true)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Format E.164 requis")
    private String phone;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;

    private String address;
    private String city;
    private String country;
    private Boolean termsAccepted;

    @Column(nullable = false)
    private boolean webAuthnEnabled = false;

    // @Column(nullable = false)
    //private boolean  = true; // Par défaut tout client est actif, uncomment later

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private Set<WebAuthnCredential> webAuthnCredentials = new HashSet<>();

    public void addWebAuthnCredential(WebAuthnCredential credential) {
        this.webAuthnCredentials.add(credential);
        credential.setClient(this);
    }
    public WebAuthnCredential getFirstWebAuthnCredential() {
        return webAuthnCredentials.iterator().next();}

    public boolean isWebAuthnEnabled() { return webAuthnEnabled; }

    @Transient // Non persisté en base
    private transient String challenge; // Stocké temporairement pour WebAuthn

    public Client() {}

    public Client(String fullName, Date dateOfBirth, String nationalId,
                  String email, String password, String phone, boolean phoneVerified,
                  String address, String city, String country, Boolean termsAccepted,
                  boolean webAuthnEnabled, Set<WebAuthnCredential> webAuthnCredentials ) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.nationalId = nationalId;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.phoneVerified = phoneVerified;
        this.address = address;
        this.city = city;
        this.country = country;
        this.termsAccepted = termsAccepted;
        this.webAuthnEnabled = webAuthnEnabled;
        this.webAuthnCredentials = webAuthnCredentials;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneVerified() { return phoneVerified; }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified; }

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

    public Boolean getTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public boolean getWebAuthnEnabled() {
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

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
