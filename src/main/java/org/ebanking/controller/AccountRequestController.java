package org.ebanking.controller;

import org.ebanking.model.AccountRequest;
import org.ebanking.service.AccountRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-requests")
public class AccountRequestController {

    @Autowired
    private AccountRequestService accountRequestService;

    @GetMapping("/pending")
    public ResponseEntity<List<AccountRequest>> getPendingRequests(@RequestHeader("agent-id") Long agentId) {
        List<AccountRequest> requests = accountRequestService.getPendingRequestsForAgent(agentId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<Void> approveRequest(@PathVariable Long requestId) {
        accountRequestService.approveRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long requestId) {
        accountRequestService.rejectRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
