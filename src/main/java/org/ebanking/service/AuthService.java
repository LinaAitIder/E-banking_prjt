package org.ebanking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.ebanking.model.Admin;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticate(String email, String password) {
        User user = em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("Invalid credentials");
        }

        // Chargement de l'entité concrète selon le type
        if (user instanceof Client) {
            return em.find(Client.class, user.getId());
        } else if (user instanceof BankAgent) {
            return em.find(BankAgent.class, user.getId());
        } else if (user instanceof Admin) {
            return em.find(Admin.class, user.getId());
        }
        return user;
    }
}

