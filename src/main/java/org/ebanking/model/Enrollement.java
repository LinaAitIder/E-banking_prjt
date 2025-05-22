package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "enrolement")
public class Enrollement {
    @Id
    @ColumnDefault("nextval('enrolement_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_agent", nullable = false)
    private BankAgent idAgent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_enrolement")
    private Instant enrollmentDate;

    @Size(max = 20)
    @ColumnDefault("'En attente'")
    @Column(name = "statut", length = 20)
    private String status;

    @Column(name = "commentaire", length = Integer.MAX_VALUE)
    private String comment;

    @Column(name = "documents")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> documents;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getclient() {
        return client;
    }

    public void setclient(Client client) {
        this.client = client;
    }

    public BankAgent getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(BankAgent idAgent) {
        this.idAgent = idAgent;
    }

    public Instant getenrollmentDate() {
        return enrollmentDate;
    }

    public void setenrollmentDate(Instant enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public String getcomment() {
        return comment;
    }

    public void setcomment(String comment) {
        this.comment = comment;
    }

    public Map<String, Object> getDocuments() {
        return documents;
    }

    public void setDocuments(Map<String, Object> documents) {
        this.documents = documents;
    }

}