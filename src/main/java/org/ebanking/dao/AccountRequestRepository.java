package org.ebanking.dao;

import org.ebanking.model.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

    List<AccountRequest> findByClientResponsibleAgentIdAndStatus(Long agentId, AccountRequest.Status status);

    @Query("SELECT ar FROM AccountRequest ar WHERE ar.client.id = :clientId AND ar.status = 'PENDING'")
    List<AccountRequest> findPendingRequestsByClientId(@Param("clientId") Long clientId);
}
