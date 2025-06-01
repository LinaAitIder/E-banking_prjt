package org.ebanking.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import java.time.Instant;

/**
 * Represents a system configuration parameter with tracking of modifications.
 */
@Entity
@Table(name = "system_parameter", uniqueConstraints = {
        @UniqueConstraint(name = "uk_system_parameter_key", columnNames = {"param_key"})
})
public class SystemParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by_admin_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Admin modifiedByAdmin;

    @NotBlank
    @Size(max = 50)
    @Column(name = "param_key", nullable = false, unique = true)
    private String key;

    @NotBlank
    @Lob
    @Column(name = "param_value", nullable = false)
    private String value;

    @Lob
    @Column(name = "description")
    private String description;

    @ColumnDefault("false")
    @Column(name = "is_system_param", nullable = false)
    private Boolean systemParameter = false;

    @ColumnDefault("false")
    @Column(name = "is_currency_param", nullable = false)
    private Boolean currencyParameter = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "parameter_type", length = 20)
    private ParameterType type;

    // Parameter type classification
    public enum ParameterType {
        SECURITY,
        BUSINESS_RULE,
        UI_CONFIG,
        CURRENCY,
        INTEGRATION
    }

    // Constructors
    public SystemParameter() {}

    public SystemParameter(String key, String value, ParameterType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    // Business Methods
    public boolean isSensitive() {
        return type == ParameterType.SECURITY;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Admin getModifiedByAdmin() { return modifiedByAdmin; }
    public void setModifiedByAdmin(Admin modifiedByAdmin) {
        this.modifiedByAdmin = modifiedByAdmin;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isSystemParameter() { return systemParameter; }
    public void setSystemParameter(Boolean systemParameter) {
        this.systemParameter = systemParameter;
    }

    public Boolean isCurrencyParameter() { return currencyParameter; }
    public void setCurrencyParameter(Boolean currencyParameter) {
        this.currencyParameter = currencyParameter;
    }

    public Instant getLastModified() { return lastModified; }
    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public ParameterType getType() { return type; }
    public void setType(ParameterType type) { this.type = type; }
}