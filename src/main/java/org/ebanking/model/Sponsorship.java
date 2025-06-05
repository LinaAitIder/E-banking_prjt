package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.math.BigDecimal;
import java.time.Instant;

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
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sponsor_id", nullable = false)
    private Client sponsor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referred_client_id", nullable = false)
    private Client referredClient;

    @Column(nullable = false, updatable = false)
    private Instant sponsorshipDate = Instant.now();

    @Column(nullable = false)
    private boolean active = true;

    @Column(precision = 15, scale = 2)
    private BigDecimal sponsorBonus = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal referredClientBonus = BigDecimal.ZERO;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true, nullable = false)
    private String referralCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SponsorshipStatus status = SponsorshipStatus.PENDING;

    @Version
    private Long version;

    // Enumération des statuts
    public enum SponsorshipStatus {
        PENDING,    // En attente de validation
        ACTIVE,     // Validé et actif
        COMPLETED,  // Parrainage terminé (bonus versés)
        CANCELLED   // Annulé
    }

    public Sponsorship() {}

    public Sponsorship(Client sponsor, Client referredClient, String referralCode) {
        this.sponsor = sponsor;
        this.referredClient = referredClient;
        this.referralCode = referralCode;
    }

    // Methodes metier
    public void applyBonus(BigDecimal sponsorAmount, BigDecimal referredAmount) {
        this.sponsorBonus = this.sponsorBonus.add(sponsorAmount);
        this.referredClientBonus = this.referredClientBonus.add(referredAmount);
        if (this.status != SponsorshipStatus.COMPLETED) {
            this.status = SponsorshipStatus.ACTIVE;
        }
    }

    public void complete() {
        this.status = SponsorshipStatus.COMPLETED;
        this.active = false;
    }

    public void cancel() {
        this.status = SponsorshipStatus.CANCELLED;
        this.active = false;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Client getSponsor() { return sponsor; }

    public void setSponsor(Client sponsor) { this.sponsor = sponsor; }

    public void setActive(boolean active) {this.active = active;}

    public Long getVersion() {return version;}

    public void setVersion(Long version) {this.version = version;}

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