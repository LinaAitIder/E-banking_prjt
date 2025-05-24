package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "bank_agent", uniqueConstraints = {
        @UniqueConstraint(name = "bank_agent_code_key", columnNames = {"agent_code"})
})
@PrimaryKeyJoinColumn(name = "user_id")
public class BankAgent extends User {

    @Size(max = 20)
    @NotNull
    @Column(name = "agent_code", nullable = false, unique = true)
    private String agentCode;

    @Size(max = 100)
    @NotNull
    @Column(name = "agency", nullable = false)
    private String agency;

    // Constructors
    public BankAgent() {}

    // Getters/setters
    public String getAgentCode() { return agentCode; }
    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }
    public String getAgency() { return agency; }
    public void setAgency(String agency) { this.agency = agency; }
}