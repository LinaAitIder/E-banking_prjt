package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "transfer")
@PrimaryKeyJoinColumn(name = "transaction_id")
public class Transfer extends Transaction {

    @Size(max = 34) // IBAN max length
    @NotNull
    @Column(name = "destination_account", nullable = false)
    private String destinationAccount;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @ColumnDefault("false")
    @Column(name = "is_instant")
    private Boolean instant = false;

    // Getters and setters
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getInstant() {
        return instant;
    }

    public void setInstant(Boolean instant) {
        this.instant = instant;
    }
}