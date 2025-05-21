package org.ebanking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ebanking.dao.ClientRepository;
import org.ebanking.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Client authenticate(String email, String password) {
        Client client = em.createQuery(
                        "SELECT c FROM Client c WHERE c.email = :email", Client.class)
                .setParameter("email", email)
                .getSingleResult();

        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new SecurityException("Invalid credentials");
        }

        return client;
    }
}
