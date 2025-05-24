package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.MessageSentiment;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents a message in an AI conversation, tracking content, authorship, and sentiment.
 */
@Entity
@Table(name = "ai_message", indexes = {
        @jakarta.persistence.Index(name = "idx_ai_message_conversation", columnList = "conversation_id")
})
public class AIMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AIConversation conversation;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp = Instant.now();

    @NotNull
    @Column(name = "is_from_user", nullable = false)
    private Boolean isFromUser = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment", length = 20)
    private MessageSentiment sentiment;

    // Constructors
    public AIMessage() {}

    public AIMessage(String content, boolean isFromUser) {
        this.content = content;
        this.isFromUser = isFromUser;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AIConversation getConversation() { return conversation; }
    public void setConversation(AIConversation conversation) {
        this.conversation = conversation;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Boolean isFromUser() { return isFromUser; }
    public void setFromUser(Boolean fromUser) { isFromUser = fromUser; }

    public MessageSentiment getSentiment() { return sentiment; }
    public void setSentiment(MessageSentiment sentiment) {
        this.sentiment = sentiment;
    }
}

