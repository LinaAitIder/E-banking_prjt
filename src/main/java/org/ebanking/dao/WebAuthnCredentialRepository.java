package org.ebanking.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ebanking.model.WebAuthnCredential;
import org.springframework.stereotype.Repository;

@Repository
public class WebAuthnCredentialRepository {

    @PersistenceContext
    private EntityManager em;

    public WebAuthnCredential save(WebAuthnCredential credential) {
        if (credential.getId() == null) {
            em.persist(credential);
        } else {
            credential = em.merge(credential);
        }
        return credential;
    }

    public WebAuthnCredential findByCredentialId(String credentialId) {
        return em.createQuery(
                        "SELECT w FROM WebAuthnCredential w WHERE w.credentialId = :credentialId",
                        WebAuthnCredential.class)
                .setParameter("credentialId", credentialId)
                .getSingleResult();
    }
}