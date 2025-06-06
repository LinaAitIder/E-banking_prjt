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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class AccountController {

    @Autowired
    private AccountService accountService;

    //Verify if this client has an account
    @GetMapping("/has-accounts")
    public ResponseEntity<Boolean> clientHasAccounts(@RequestHeader("clientId")  Long clientId) {
        boolean hasAccounts = accountService.clientHasAccounts(clientId);
        return ResponseEntity.ok(hasAccounts);
    }


    // Create account
    @PostMapping("/createAccount")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest accountRequest
    ) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }



    // get client accounts
    @GetMapping("/getAccount")
    public ResponseEntity<List<Account>> getClientAccounts(@RequestHeader("clientId") Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClientId(clientId));
    }


    // desactivate account
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deactivateAccount(@RequestHeader("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}