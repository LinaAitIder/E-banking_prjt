package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.ebanking.model.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "agent_banque", uniqueConstraints = {
        @UniqueConstraint(name = "agent_banque_code_agent_key", columnNames = {"code_agent"})
})
public class BankAgent extends User{
    @Id
    @Column(name = "id_agent", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_agent", nullable = false)
    private User user;

    @Size(max = 20)
    @NotNull
    @Column(name = "code_agent", nullable = false, length = 20)
    private String agentCode;

    @Size(max = 100)
    @NotNull
    @Column(name = "agence", nullable = false, length = 100)
    private String agency;

    @Override
    public List<String> getRoles() {
        return List.of("ROLE_BANKAGENT");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUtilisateur() {
        return user;
    }

    public void setUtilisateur(User user) {
        this.user = user;
    }

    public String getagentCode() {
        return agentCode;
    }

    public void setagentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getagency() {
        return agency;
    }

    public void setagency(String agency) {
        this.agency = agency;
    }

}