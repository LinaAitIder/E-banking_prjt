package org.ebanking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * AI Assistant entity representing conversational AI capabilities.
 * Manages supported languages and conversation history.
 */
@Entity
@Table(name = "ai_assistant")
public class AIAssistant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "supported_languages", columnDefinition = "jsonb default '[\"EN\", \"FR\"]'")
    private List<String> supportedLanguages;

    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant creationDate = Instant.now();

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "assistant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AIConversation> conversations = new LinkedHashSet<>();

    // Constructors
    public AIAssistant() {}

    public AIAssistant(String modelName, List<String> supportedLanguages) {
        this.modelName = modelName;
        this.supportedLanguages = supportedLanguages;
    }

    // Business Methods
    public void addConversation(AIConversation conversation) {
        conversations.add(conversation);
        conversation.setAssistant(this);
    }

    public void removeConversation(AIConversation conversation) {
        conversations.remove(conversation);
        conversation.setAssistant(null);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public List<String> getSupportedLanguages() { return supportedLanguages; }
    public void setSupportedLanguages(List<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public Instant getCreationDate() { return creationDate; }
    public void setCreationDate(Instant creationDate) { this.creationDate = creationDate; }

    public Boolean getActive() { return isActive; }
    public void setActive(Boolean active) { isActive = active; }

    public Set<AIConversation> getConversations() { return conversations; }
    protected void setConversations(Set<AIConversation> conversations) {
        this.conversations = conversations;
    }
}