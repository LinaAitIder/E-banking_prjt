package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.ActionType;
import org.ebanking.model.enums.SecurityLevel;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents an audit log entry tracking system activities and user actions.
 **/
@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_audit_log_user", columnList = "user_id"),
        @Index(name = "idx_audit_log_action_date", columnList = "action_date"),
        @Index(name = "idx_audit_affected_entity", columnList = "affected_entity_type,affected_entity_id"),
        @Index(name = "idx_audit_security", columnList = "security_level")
})
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "action_date", nullable = false)
    private Instant actionDate = Instant.now();

    @NotNull
    @Size(max = 255)
    @Column(name = "action", nullable = false)
    private String action;

    @NotNull
    @Size(max = 45)
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Size(max = 50)
    @Column(name = "result")
    private String result;

    @Lob
    @Column(name = "details")
    private String details;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 20)
    private ActionType actionType;

    // Nouveaux champs pour RGPD et traçabilité avancée
    @Column(name = "contains_personal_data", nullable = false)
    private boolean containsPersonalData = false;

    @Column(name = "affected_entity_type", length = 50)
    private String affectedEntityType;

    @Column(name = "affected_entity_id")
    private Long affectedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_level", length = 10)
    private SecurityLevel securityLevel = SecurityLevel.MEDIUM;

    @Size(max = 36)
    @Column(name = "consent_reference")
    private String consentReference;

    // constructor
    public AuditLog() {}

    public AuditLog(String action, String ipAddress) {
        this.action = action;
        this.ipAddress = ipAddress;
    }

    // useful methods
    public void markHighSecurity() {
        this.securityLevel = SecurityLevel.HIGH;
    }

    public void linkToEntity(String entityType, Long entityId) {
        this.affectedEntityType = entityType;
        this.affectedEntityId = entityId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getActionDate() { return actionDate; }
    public void setActionDate(Instant actionDate) { this.actionDate = actionDate; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public boolean isContainsPersonalData() { return containsPersonalData; }
    public void setContainsPersonalData(boolean containsPersonalData) {
        this.containsPersonalData = containsPersonalData;
    }

    public String getAffectedEntityType() { return affectedEntityType; }
    public void setAffectedEntityType(String affectedEntityType) {
        this.affectedEntityType = affectedEntityType;
    }

    public Long getAffectedEntityId() { return affectedEntityId; }
    public void setAffectedEntityId(Long affectedEntityId) {
        this.affectedEntityId = affectedEntityId;
    }

    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getConsentReference() { return consentReference; }
    public void setConsentReference(String consentReference) {
        this.consentReference = consentReference;
    }
}