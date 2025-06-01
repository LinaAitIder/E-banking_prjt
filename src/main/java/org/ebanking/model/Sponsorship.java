package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a sponsorship relationship between clients with referral bonuses.
 */
@Entity
@Table(name = "sponsorship", indexes = {
        @Index(name = "idx_sponsorship_sponsor", columnList = "sponsor_id"),
        @Index(name = "idx_sponsorship_referred", columnList = "referred_client_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_sponsorship_relationship", columnNames = {"sponsor_id", "referred_client_id"}),
        @UniqueConstraint(name = "uk_sponsorship_code", columnNames = {"referral_code"})
})
public class Sponsorship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sponsor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client sponsor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referred_client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client referredClient;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "sponsorship_date", nullable = false)
    private Instant sponsorshipDate = Instant.now();

    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @ColumnDefault("0.00")
    @Column(name = "sponsor_bonus", precision = 15, scale = 2)
    private BigDecimal sponsorBonus = BigDecimal.ZERO;

    @ColumnDefault("0.00")
    @Column(name = "referred_client_bonus", precision = 15, scale = 2)
    private BigDecimal referredClientBonus = BigDecimal.ZERO;

    @NotBlank
    @Size(max = 20)
    @Column(name = "referral_code", nullable = false, unique = true)
    private String referralCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SponsorshipStatus status = SponsorshipStatus.PENDING;

    // Sponsorship status
    public enum SponsorshipStatus {
        PENDING,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

    // Constructors
    public Sponsorship() {}

    public Sponsorship(Client sponsor, Client referredClient, String referralCode) {
        this.sponsor = sponsor;
        this.referredClient = referredClient;
        this.referralCode = referralCode;
    }

    // Business Methods
    public void applyBonus(BigDecimal sponsorAmount, BigDecimal referredAmount) {
        this.sponsorBonus = this.sponsorBonus.add(sponsorAmount);
        this.referredClientBonus = this.referredClientBonus.add(referredAmount);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getSponsor() { return sponsor; }
    public void setSponsor(Client sponsor) { this.sponsor = sponsor; }

    public Client getReferredClient() { return referredClient; }
    public void setReferredClient(Client referredClient) {
        this.referredClient = referredClient;
    }

    public Instant getSponsorshipDate() { return sponsorshipDate; }
    public void setSponsorshipDate(Instant sponsorshipDate) {
        this.sponsorshipDate = sponsorshipDate;
    }

    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public BigDecimal getSponsorBonus() { return sponsorBonus; }
    public void setSponsorBonus(BigDecimal sponsorBonus) {
        this.sponsorBonus = sponsorBonus;
    }

    public BigDecimal getReferredClientBonus() { return referredClientBonus; }
    public void setReferredClientBonus(BigDecimal referredClientBonus) {
        this.referredClientBonus = referredClientBonus;
    }

    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public SponsorshipStatus getStatus() { return status; }
    public void setStatus(SponsorshipStatus status) { this.status = status; }
}