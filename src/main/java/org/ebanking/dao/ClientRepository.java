package org.ebanking.dao;

import org.ebanking.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByNationalId(String nationalId);

    Client findByEmail(String email);

}