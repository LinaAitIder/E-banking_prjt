package org.ebanking.dao;

import org.ebanking.model.Client;
import org.ebanking.model.Enrollment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @EntityGraph(attributePaths = {"client", "client.accounts"})
    List<Enrollment> findByAgentId(Long agentId);

    Optional<Enrollment> findByClientId(Long clientId);

    @Query("SELECT c FROM Client c WHERE c.responsibleAgent.id = :agentId AND c.id IN " +
            "(SELECT e.client.id FROM Enrollment e WHERE e.agent.id = :agentId)")
    List<Client> findClientsByAgent(@Param("agentId") Long agentId);

}