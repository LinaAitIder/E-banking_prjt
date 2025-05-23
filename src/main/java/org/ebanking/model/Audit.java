package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "audit", indexes = {
        @Index(name = "idx_audit_utilisateur", columnList = "id_utilisateur"),
        @Index(name = "idx_audit_date", columnList = "date_action")
})
public class Audit {
    @Id
    @ColumnDefault("nextval('audit_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_utilisateur")
    private User idUser;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_action")
    private Instant actionDate;

    @Size(max = 255)
    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @Size(max = 45)
    @NotNull
    @Column(name = "adresse_ip", nullable = false, length = 45)
    private String ipAddress;

    @Size(max = 50)
    @Column(name = "resultat", length = 50)
    private String result;

    @Column(name = "details", length = Integer.MAX_VALUE)
    private String details;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getIdUtilisateur() {
        return idUser;
    }

    public void setIdUtilisateur(User idUser) {
        this.idUser = idUser;
    }

    public Instant getactionDate() {
        return actionDate;
    }

    public void setactionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getipAddress() {
        return ipAddress;
    }

    public void setipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getresult() {
        return result;
    }

    public void setresult(String result) {
        this.result = result;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}