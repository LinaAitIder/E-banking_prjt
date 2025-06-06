package org.ebanking.controller;

import org.ebanking.dto.request.AccountRequest;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    //Verify if this client has an account
    @GetMapping("/has-accounts")
    public ResponseEntity<Boolean> clientHasAccounts(@RequestHeader("client-id")  Long clientId) {
        boolean hasAccounts = accountService.clientHasAccounts(clientId);
        return ResponseEntity.ok(hasAccounts);
    }


    // Create account
    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestHeader("user-id") Long userId,
            @RequestBody AccountRequest accountRequest) {

        AccountResponse createdAccount = accountService.createAccount(userId, accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/details")
    public ResponseEntity<AccountResponse> getAccountDetails(
            @RequestHeader("account-id") Long accountId) {
        AccountResponse response = accountService.getAccountDetails(accountId);
        return ResponseEntity.ok(response);
    }


    // Désactivation
    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateAccount(@RequestHeader("account-id") Long accountId) {
        accountService.deactivateAccount(accountId);
        return ResponseEntity.ok().build();
    }

    // Suppression
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@RequestHeader("account-id") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}