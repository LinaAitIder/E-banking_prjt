package org.ebanking.controller;

import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.Account;
import org.ebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Création d'un compte (le type est déterminé par la sous-classe dans le JSON)
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest accountRequest
    ) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    // Récupération des comptes d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Account>> getClientAccounts(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClientId(clientId));
    }

    // suppression d'un compte
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deactivateAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}