package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents a two-factor authentication (2FA) record for user security.
 * Stores verification methods and temporary codes with expiration.
 */
@Entity
@Table(name = "two_factor_authentication")
public class TwoFactorAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_method", nullable = false, length = 20)
    private AuthenticationMethod method;

    @Size(max = 8)
    @Column(name = "verification_code", length = 8)
    private String verificationCode;

    @Column(name = "expiration_time")
    private Instant expirationTime;

    @ColumnDefault("false")
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // Authentication methods
    public enum AuthenticationMethod {
        SMS,
        EMAIL,
        AUTHENTICATOR_APP,
        SECURITY_KEY
    }

    // Constructors
    public TwoFactorAuthentication() {}

    public TwoFactorAuthentication(User user, AuthenticationMethod method) {
        this.user = user;
        this.method = method;
    }

    // Business Methods
    public boolean isCodeValid() {
        return !isUsed && expirationTime != null &&
                Instant.now().isBefore(expirationTime);
    }

    public void markAsUsed() {
        this.isUsed = true;
        this.verificationCode = null;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AuthenticationMethod getMethod() { return method; }
    public void setMethod(AuthenticationMethod method) { this.method = method; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Instant getExpirationTime() { return expirationTime; }
    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Boolean isUsed() { return isUsed; }
    public void setUsed(Boolean used) { isUsed = used; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}