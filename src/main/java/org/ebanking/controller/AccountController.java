package org.ebanking.controller;

import org.ebanking.dto.request.AccountRequestDto;
import org.ebanking.dto.response.AccountResponse;
import org.ebanking.model.enums.AccountType;
import org.ebanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody AccountRequestDto accountRequest) {

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

    //getAllAccounts
    @RequestMapping(value = "/client/{clientId}/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<AccountResponse>> getAccountsByType(
            @PathVariable("clientId") Long clientId,
            @RequestParam(value = "type", required = false) AccountType type) {

        List<AccountResponse> accounts = accountService.getAccountsByClientAndType(clientId, type);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/has-savings")
    public ResponseEntity<Boolean> clientHasSavingsAccount(@RequestHeader("client-id") Long clientId) {
        boolean hasSavings = accountService.clientHasSavingsAccount(clientId);
        return ResponseEntity.ok(hasSavings);
    }
}