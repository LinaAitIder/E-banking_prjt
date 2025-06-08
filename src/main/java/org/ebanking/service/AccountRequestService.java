package org.ebanking.service;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRequestRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.model.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class AccountRequestService {

    @Autowired
    private AccountRequestRepository accountRequestRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    public AccountRequest createRequest(AccountRequest request) {
        return accountRequestRepository.save(request);
    }

    public List<AccountRequest> getPendingRequestsForAgent(Long agentId) {
        return accountRequestRepository.findByClientResponsibleAgentIdAndStatus(agentId, AccountRequest.Status.PENDING);
    }

    public void approveRequest(Long requestId) {
        // 1. Récupérer la demande depuis la base (Entity)
        org.ebanking.model.AccountRequest entity = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        // 2. Convertir l'Entity en DTO
        org.ebanking.dto.request.AccountRequest dto = new org.ebanking.dto.request.AccountRequest();

        // Conversion du type de compte (Enum -> String)
        dto.setAccountType(entity.getAccountType().name());
        dto.setCurrency(entity.getCurrency());

        // Remplissage des champs specifiques au type de compte
        switch (entity.getAccountType()) {
            case CURRENT:
                dto.setOverdraftLimit(entity.getOverdraftLimit());
                break;
            case SAVINGS:
                dto.setInterestRate(entity.getInterestRate());
                break;
            case CRYPTO:
                dto.setSupportedCryptos(new HashMap<>()); // a adapter quand on progresse dans le crypto
                break;
        }

        // 3. Appeler le service de creation de compte avec le DTO
        accountService.createAccount(entity.getClient().getId(), dto);

        // 4. Mettre a jour le statut de la demande
        entity.setStatus(org.ebanking.model.AccountRequest.Status.APPROVED);
        accountRequestRepository.save(entity);
    }

    public void rejectRequest(Long requestId) {
        AccountRequest request = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));
        request.setStatus(AccountRequest.Status.REJECTED);
        accountRequestRepository.save(request);
    }
}
