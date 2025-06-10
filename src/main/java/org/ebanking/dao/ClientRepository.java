package org.ebanking.dao;

import org.ebanking.model.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //@EntityGraph(attributePaths = {"user"})
    Optional<Client> findById(Long id);

    @Query("SELECT c FROM Client c WHERE c.isEnrolled = false")
    List<Client> findByIsEnrolledFalse();

    @Query("SELECT c FROM Client c WHERE c.responsibleAgent.id = :agentId")
    List<Client> findByResponsibleAgentId(@Param("agentId") Long agentId);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.accounts WHERE c.id = :clientId")
    Optional<Client> findByIdWithAccounts(@Param("clientId") Long clientId);

        @Query("SELECT c FROM Client c LEFT JOIN FETCH c.responsibleAgent")
        List<Client> findAllWithAgent();

}
