package org.ebanking.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "assistant_ia")
public class AIAssistant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "langues_supports", columnDefinition = "jsonb default '[\"FR\", \"EN\"]'")
    private List<String> supportedLanguages;

    @Column(name = "date_creation", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Instant creationDate;

    @Column(name = "est_actif", columnDefinition = "boolean default true")
    private Boolean isActive;

    @OneToMany(mappedBy = "idAssistant")
    private Set<org.ebanking.model.AIConversation> AIConversations = new LinkedHashSet<>();

    public Set<org.ebanking.model.AIConversation> getAIConversations() {
        return AIConversations;
    }

    public void setAIConversations(Set<org.ebanking.model.AIConversation> AIConversations) {
        this.AIConversations = AIConversations;
    }

    // Constructors
    public AIAssistant() {
    }

    public AIAssistant(String model, List<String> supportedLanguages) {
        this.model = model;
        this.supportedLanguages = supportedLanguages;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(List<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }


}