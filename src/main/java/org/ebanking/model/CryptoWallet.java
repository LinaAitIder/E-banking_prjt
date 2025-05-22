package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "portefeuille_crypto", indexes = {
        @Index(name = "idx_portefeuille_compte", columnList = "id_compte")
}, uniqueConstraints = {
        @UniqueConstraint(name = "portefeuille_crypto_id_compte_key", columnNames = {"id_compte"}),
        @UniqueConstraint(name = "portefeuille_crypto_adresse_key", columnNames = {"adresse"})
})
public class CryptoWallet {
    @Id
    @ColumnDefault("nextval('portefeuille_crypto_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_compte")
    private Account idAccount;

    @Size(max = 255)
    @NotNull
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @ColumnDefault("'[]'")
    @Column(name = "crypto_monnaies")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> CryptoCurrency;

    @Column(name = "api_key", length = Integer.MAX_VALUE)
    private String apiKey;

    @Size(max = 50)
    @Column(name = "exchange", length = 50)
    private String exchange;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getIdCompte() {
        return idAccount;
    }

    public void setIdCompte(Account idAccount) {
        this.idAccount = idAccount;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Map<String, Object> getCryptoCurrency() {
        return CryptoCurrency;
    }

    public void setCryptoCurrency(Map<String, Object> CryptoCurrency) {
        this.CryptoCurrency = CryptoCurrency;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

}