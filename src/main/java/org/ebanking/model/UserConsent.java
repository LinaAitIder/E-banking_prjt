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
    private String consentType;

    @Column(nullable = false)
    private String reference; // ID unique (ex: "CONSENT-2023-XYZ123")

    @Column(nullable = false)
    private Instant givenAt; // Date/heure du consentement

    @Column(nullable = false)
    private boolean isActive = true;

    // Constructeurs, Getters/Setters...



}