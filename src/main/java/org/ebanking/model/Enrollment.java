package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.EnrollmentStatus;
import org.ebanking.model.enums.DocumentType;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a client enrollment process with a bank agent.
 * Tracks enrollment status, documents, and agent assignment
 */
@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private BankAgent agent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "enrollment_date", nullable = false)
    private Instant enrollmentDate = Instant.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Lob
    @Column(name = "comments")
    private String comments;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "documents", columnDefinition = "jsonb")
    private Map<String, String> documents;  // Map<DocumentType, DocumentReference>


    // Constructors
    public Enrollment() {}

    // Business Methods
    public void addDocument(DocumentType type, String reference) {
        this.documents.put(type.name(), reference);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public BankAgent getAgent() { return agent; }
    public void setAgent(BankAgent agent) { this.agent = agent; }

    public Instant getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Instant enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public Map<String, String> getDocuments() { return documents; }
    public void setDocuments(Map<String, String> documents) {
        this.documents = documents;
    }
}