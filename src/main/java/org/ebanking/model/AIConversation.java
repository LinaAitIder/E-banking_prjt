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
@Table(name = "conversation_ia", indexes = {
        @Index(name = "idx_conversation_client", columnList = "id_client")
})
public class AIConversation {
    @Id
    @ColumnDefault("nextval('conversation_ia_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_assistant", nullable = false)
    private AIAssistant idAssistant;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "date_debut")
    private Instant startDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "dernier_echange")
    private Instant lastExchange;

    @Size(max = 100)
    @Column(name = "sujet", length = 100)
    private String subject;

    @Size(max = 2)
    @ColumnDefault("'FR'")
    @Column(name = "langue", length = 2)
    private String language;

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

    public AIAssistant getIdAssistant() {
        return idAssistant;
    }

    public void setIdAssistant(AIAssistant idAssistant) {
        this.idAssistant = idAssistant;
    }

    public Instant getstartDate() {
        return startDate;
    }

    public void setstartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getlastExchange() {
        return lastExchange;
    }

    public void setlastExchange(Instant lastExchange) {
        this.lastExchange = lastExchange;
    }

    public String getsubject() {
        return subject;
    }

    public void setsubject(String subject) {
        this.subject = subject;
    }

    public String getlanguage() {
        return language;
    }

    public void setlanguage(String language) {
        this.language = language;
    }

}