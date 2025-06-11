package org.ebanking.controller;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dto.request.AccountRequestDto;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.AccountRequest;
import org.ebanking.model.Client;
import org.ebanking.model.enums.AccountType;
import org.ebanking.service.AccountRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-requests")
public class AccountRequestController {

    @Autowired
    private AccountRequestService accountRequestService;

    @Autowired
    private ClientRepository clientRepository;


    @PostMapping
    public ResponseEntity<AccountRequestDto> createRequest(
            @RequestHeader("client-id") Long clientId,
            @RequestBody AccountRequestDto requestDto) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (!client.isEnrolled()) {
            throw new IllegalStateException("Client must be enrolled to request an account");
        }

        AccountRequest createdRequest = accountRequestService.createRequest(clientId, requestDto);
        AccountRequestDto responseDto = convertToDto(createdRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AccountRequestDto>> getPendingRequests(@RequestHeader("agent-id") Long agentId) {
        List<AccountRequestDto> requests = accountRequestService.getPendingRequestsForAgent(agentId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<AccountResponse> approveRequest(
            @PathVariable Long requestId,
            @RequestHeader("agent-id") Long agentId) {

        AccountResponse createdAccount = accountRequestService.approveRequest(requestId, agentId);
        return ResponseEntity.ok(createdAccount);
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable Long requestId,
            @RequestHeader("agent-id") Long agentId) {

        accountRequestService.rejectRequest(requestId, agentId);
        return ResponseEntity.ok().build();
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
