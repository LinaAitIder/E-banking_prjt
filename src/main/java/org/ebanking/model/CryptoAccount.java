package org.ebanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "compte_crypto")
public class CryptoAccount {
    @Id
    @Column(name = "id_compte", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_compte", nullable = false)
    private Account account;

    @Column(name = "cryptos_supports")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> cryptosSupports;

    @Size(max = 255)
    @Column(name = "adresse_portefeuille")
    private String walletAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getCompte() {
        return account;
    }

    public void setCompte(Account account) {
        this.account = account;
    }

    public Map<String, Object> getCryptosSupports() {
        return cryptosSupports;
    }

    public void setCryptosSupports(Map<String, Object> cryptosSupports) {
        this.cryptosSupports = cryptosSupports;
    }

    public String getwalletAddress() {
        return walletAddress;
    }

    public void setwalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

}