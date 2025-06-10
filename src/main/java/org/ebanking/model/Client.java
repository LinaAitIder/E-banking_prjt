package org.ebanking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {

    @Column(name = "is_enrolled", nullable = false)
    private boolean isEnrolled = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsible_agent_id")
    private BankAgent responsibleAgent;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<WebAuthnCredential> webAuthnCredentials = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Account> accounts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "main_account_id")
    @JsonIgnore
    private Account mainAccount;

    public Account getMainAccount() {
        if (this.mainAccount == null && !this.accounts.isEmpty()) {
            return this.accounts.get(0); // Retourne le premier compte par defaut
        }
        return this.mainAccount;
    }

    public void setMainAccount(Account account) {
        if (account != null && !this.accounts.contains(account)) {
            throw new IllegalArgumentException("Account does not belong to client");
        }
        this.mainAccount = account;
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        account.setOwner(this);
        this.accounts.add(account);
        if (this.mainAccount == null) {
            this.mainAccount = account;
        }
    }

    public Client() {}

    // Additional methods
    public void addWebAuthnCredential(WebAuthnCredential credential) {
        this.webAuthnCredentials.add(credential);
        credential.setUser(this);}

    @Override
    public List<String> getRoles() {
        return List.of("ROLE_CLIENT");
    }



    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public BankAgent getResponsibleAgent() {
        return responsibleAgent;
    }

    public void setResponsibleAgent(BankAgent responsibleAgent) {
        this.responsibleAgent = responsibleAgent;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

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

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}