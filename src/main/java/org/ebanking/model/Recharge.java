package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.ebanking.model.enums.Operator;
import org.ebanking.model.enums.ServiceType;

@Entity
@Table(name = "recharge")
@PrimaryKeyJoinColumn(name = "transaction_id")
public class Recharge extends Transaction {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @Size(max = 50)
    @NotNull
    @Column(name = "recharge_number", nullable = false)
    private String rechargeNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operator", nullable = false)
    private Operator operator;

    // Getters and setters
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }
}

