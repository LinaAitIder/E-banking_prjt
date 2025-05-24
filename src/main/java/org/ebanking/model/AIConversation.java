package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents a conversation between a client and an AI assistant.
 * Tracks conversation metadata and interaction history.
 */
@Entity
@Table(name = "ai_conversation", indexes = {
        @Index(name = "idx_ai_conversation_client", columnList = "client_id")
})
public class AIConversation {

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
    @JoinColumn(name = "assistant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AIAssistant assistant;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "start_date", nullable = false)
    private Instant startDate = Instant.now();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_exchange", nullable = false)
    private Instant lastExchange = Instant.now();

    @Size(max = 100)
    @Column(name = "subject", length = 100)
    private String subject;

    @Size(max = 2)
    @ColumnDefault("'EN'")
    @Column(name = "language", length = 2)
    private String language = "EN";

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AIMessage> messages = new LinkedHashSet<>();

    // Constructors
    public AIConversation() {}

    public AIConversation(Client client, AIAssistant assistant) {
        this.client = client;
        this.assistant = assistant;
    }

    // Business Methods
    public void updateLastExchange() {
        this.lastExchange = Instant.now();
    }

    public void addMessage(AIMessage message) {
        messages.add(message);
        message.setConversation(this);
        updateLastExchange();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public AIAssistant getAssistant() { return assistant; }
    public void setAssistant(AIAssistant assistant) { this.assistant = assistant; }

    public Instant getStartDate() { return startDate; }
    public void setStartDate(Instant startDate) { this.startDate = startDate; }

    public Instant getLastExchange() { return lastExchange; }
    public void setLastExchange(Instant lastExchange) { this.lastExchange = lastExchange; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public Set<AIMessage> getMessages() { return messages; }
    protected void setMessages(Set<AIMessage> messages) { this.messages = messages; }
}