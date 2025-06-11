package org.ebanking.service;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRequestRepository;
import org.ebanking.dao.BankAgentRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dto.request.AccountRequestDto;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.AccountRequest;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.ebanking.model.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountRequestService {

    @Autowired
    private AccountRequestRepository accountRequestRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankAgentRepository bankAgentRepository;

    public AccountRequest createRequest(Long clientId, AccountRequestDto requestDto) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.isEnrolled()) {
            throw new IllegalStateException("Client must be enrolled to request an account");
        }

        AccountRequest request = new AccountRequest();
        request.setClient(client);
        request.setAccountType(AccountType.valueOf(requestDto.getAccountType()));
        request.setCurrency(requestDto.getCurrency());
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(AccountRequest.Status.PENDING);

        // Set type-specific fields
        if (requestDto.getAccountType().equals("CURRENT")) {
            request.setOverdraftLimit(requestDto.getOverdraftLimit());
        } else if (requestDto.getAccountType().equals("SAVINGS")) {
            request.setInterestRate(requestDto.getInterestRate());
        }

        return accountRequestRepository.save(request);
    }

    public List<AccountRequestDto> getPendingRequestsForAgent(Long agentId) {
        List<AccountRequest> requests = accountRequestRepository
                .findByResponsibleAgentAndPendingStatus(agentId);

        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AccountResponse approveRequest(Long requestId, Long agentId) {
        AccountRequest request = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        // Verify the agent is the responsible agent for this client
        if (!request.getClient().getResponsibleAgent().getId().equals(agentId)) {
            throw new IllegalStateException("Only the responsible agent can approve requests");
        }

        // Convert to AccountRequest DTO
        AccountRequestDto dto = convertToDto(request);

        // Create the account
        AccountResponse accountResponse = accountService.createAccount(request.getClient().getId(), dto);

        // Update request status
        request.setStatus(AccountRequest.Status.APPROVED);
        accountRequestRepository.save(request);

        return accountResponse;
    }

    public void rejectRequest(Long requestId, Long agentId) {
        AccountRequest request = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getClient().getResponsibleAgent().getId().equals(agentId)) {
            throw new IllegalStateException("Only the responsible agent can reject requests");
        }

        request.setStatus(AccountRequest.Status.REJECTED);
        accountRequestRepository.save(request);
    }

    private AccountRequestDto convertToDto(AccountRequest request) {
        AccountRequestDto dto = new AccountRequestDto();
        dto.setId(request.getId());
        dto.setClientId(request.getClient().getId());
        dto.setClientName(request.getClient().getFullName());
        dto.setAccountType(request.getAccountType().name());
        dto.setCurrency(request.getCurrency());
        dto.setRequestDate(request.getRequestDate());

        if (request.getAccountType() == AccountType.CURRENT) {
            dto.setOverdraftLimit(request.getOverdraftLimit());
        } else if (request.getAccountType() == AccountType.SAVINGS) {
            dto.setInterestRate(request.getInterestRate());
        }

        return dto;
    }
}
