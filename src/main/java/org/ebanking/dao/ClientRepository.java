package org.ebanking.dao;

import org.ebanking.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class ClientRepository {

    @PersistenceContext
    private EntityManager em;

    public Client save(Client client) {
        if (client.getId() == null) {
            em.persist(client);
        } else {
            client = em.merge(client);
        }
        return client;
    }

    public Client findByEmail(String email) {
        return em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public boolean existsByNationalId(String nationalId) {
        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Client c WHERE c.nationalId = :nationalId", Long.class)
                .setParameter("nationalId", nationalId)
                .getSingleResult();
        return count > 0;
    }

}