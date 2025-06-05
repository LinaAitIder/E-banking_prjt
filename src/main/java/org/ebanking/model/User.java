package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "user_email_key", columnNames = {"email"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "full_name")
    private String fullName;


    @Email
    @NotNull
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password",nullable = false)
    private String password;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(nullable = false)
    private boolean webAuthnEnabled = false;

    @Transient
    private transient String challenge;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<WebAuthnCredential> webAuthnCredentials = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public abstract List<String> getRoles();

    public String getRole() {
        if (this instanceof Client) return "CLIENT";
        if (this instanceof BankAgent) return "AGENT";
        if (this instanceof Admin) return "ADMIN";
        return "USER";
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setisActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isWebAuthnEnabled() { return webAuthnEnabled; }

    public void setWebAuthnEnabled(boolean webAuthnEnabled) {
        this.webAuthnEnabled = webAuthnEnabled;
    }
}