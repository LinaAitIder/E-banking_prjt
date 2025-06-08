package org.ebanking.dao;

import org.ebanking.model.Client;
import org.ebanking.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByAgentId(Long agentId);

    Optional<Enrollment> findByClientId(Long clientId);

    @Query("SELECT c FROM Client c WHERE c NOT IN (SELECT e.client FROM Enrollment e)")
    List<Client> findClientsWithoutEnrollment();
}