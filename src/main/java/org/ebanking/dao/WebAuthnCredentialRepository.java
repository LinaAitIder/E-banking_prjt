package org.ebanking.dao;

import org.ebanking.model.WebAuthnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAuthnCredentialRepository extends JpaRepository<WebAuthnCredential, Long> {

    WebAuthnCredential findByCredentialId(String credentialId);
}