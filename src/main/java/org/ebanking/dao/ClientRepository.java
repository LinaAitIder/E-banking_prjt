package org.ebanking.dao;

import org.ebanking.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Client> findById(Long id);

    // Charge le client avec ses comptes
    @EntityGraph(attributePaths = {"accounts"})
    Optional<Client> findWithAccountsById(Long id);
}
