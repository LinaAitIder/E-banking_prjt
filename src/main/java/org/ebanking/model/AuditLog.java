package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.ActionType;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents an audit log entry tracking system activities and user actions.
 */
@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_audit_log_user", columnList = "user_id"),
        @Index(name = "idx_audit_log_action_date", columnList = "action_date")
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


    // Constructors
    public AuditLog() {}

    public AuditLog(String action, String ipAddress) {
        this.action = action;
        this.ipAddress = ipAddress;
    }

    // Getters and Setters
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
}