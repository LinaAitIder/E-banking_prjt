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
@Table(name = "message_ia", indexes = {
        @Index(name = "idx_message_conversation", columnList = "id_conversation")
})
public class AIMessage {
    @Id
    @ColumnDefault("nextval('message_ia_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_conversation", nullable = false)
    private AIConversation idConversation;

    @NotNull
    @Column(name = "contenu", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "horodatage")
    private Instant timestamp;

    @NotNull
    @Column(name = "est_utilisateur", nullable = false)
    private Boolean isUser  = false;

    @Size(max = 20)
    @Column(name = "sentiment", length = 20)
    private String sentiment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AIConversation getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(AIConversation idConversation) {
        this.idConversation = idConversation;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public Instant gettimestamp() {
        return timestamp;
    }

    public void settimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getisUser () {
        return isUser ;
    }

    public void setisUser (Boolean isUser ) {
        this.isUser  = isUser ;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

}