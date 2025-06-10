package org.ebanking.controller;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.model.*;
import org.ebanking.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/clients/unenrolled")
    public ResponseEntity<List<Client>> getUnenrolledClients() {
        List<Client> clients = agentService.getUnenrolledClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/enroll/{clientId}")
    public ResponseEntity<Void> enrollClient(@PathVariable Long clientId,
                                             @RequestHeader("agent-id") Long agentId) {
        agentService.createEnrollment(agentId, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/clients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAgentClients(@RequestHeader("agent-id") Long agentId) {
        try {
            List<Client> clients = agentService.getAgentClients(agentId);

            // Créez un DTO pour éviter les problèmes de sérialisation
            List<Map<String, Object>> response = clients.stream().map(client -> {
                Map<String, Object> clientMap = new HashMap<>();
                clientMap.put("id", client.getId());
                clientMap.put("fullName", client.getFullName());
                clientMap.put("email", client.getEmail());
                clientMap.put("phone", client.getPhone());

                if (client.getResponsibleAgent() != null) {
                    Map<String, Object> agentMap = new HashMap<>();
                    agentMap.put("id", client.getResponsibleAgent().getId());
                    agentMap.put("fullName", client.getResponsibleAgent().getFullName());
                    agentMap.put("agentCode", client.getResponsibleAgent().getAgentCode());
                    clientMap.put("responsibleAgent", agentMap);
                }

                if (client.getAccounts() != null) {
                    clientMap.put("accounts", client.getAccounts().stream().map(account -> {
                        Map<String, Object> accountMap = new HashMap<>();
                        accountMap.put("id", account.getId());
                        accountMap.put("accountNumber", account.getAccountNumber());
                        accountMap.put("type", account.getType());
                        accountMap.put("balance", account.getBalance());
                        accountMap.put("currency", account.getCurrency());
                        return accountMap;
                    }).collect(Collectors.toList()));
                }

                return clientMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error", "details", e.getMessage()));
        }
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<Map<String, Object>> getClientDetails(@PathVariable Long clientId) {
        try {
            Client client = agentService.getClientDetails(clientId);

            // Construction de la reponse structuree
            Map<String, Object> response = new LinkedHashMap<>();

            response.put("id", client.getId());
            response.put("fullName", client.getFullName());
            response.put("email", client.getEmail());
            response.put("phone", client.getPhone());
            response.put("isEnrolled", client.isEnrolled());
            response.put("dateOfBirth", client.getDateOfBirth());
            response.put("nationalId", client.getNationalId());

            // Gestion de l'agent responsable
            if (client.getResponsibleAgent() != null) {
                Map<String, Object> agentInfo = new LinkedHashMap<>();
                agentInfo.put("id", client.getResponsibleAgent().getId());
                agentInfo.put("fullName", client.getResponsibleAgent().getFullName());
                agentInfo.put("agentCode", client.getResponsibleAgent().getAgentCode());
                agentInfo.put("agency", client.getResponsibleAgent().getAgency());
                response.put("responsibleAgent", agentInfo);
            }

            // Gestion des comptes
            if (client.getAccounts() != null && !client.getAccounts().isEmpty()) {
                List<Map<String, Object>> accountsInfo = client.getAccounts().stream()
                        .map(account -> {
                            Map<String, Object> accountMap = new LinkedHashMap<>();
                            accountMap.put("id", account.getId());
                            accountMap.put("accountNumber", account.getAccountNumber());
                            accountMap.put("type", account.getType().name()); // Utilisez l'enum
                            accountMap.put("balance", account.getBalance());
                            accountMap.put("currency", account.getCurrency());
                            accountMap.put("createdAt", account.getCreatedAt());

                            // Gestion des sous-classes
                            if (account instanceof CurrentAccount) {
                                CurrentAccount current = (CurrentAccount) account;
                                accountMap.put("overdraftLimit", current.getOverdraftLimit());
                            } else if (account instanceof SavingsAccount) {
                                SavingsAccount savings = (SavingsAccount) account;
                                accountMap.put("interestRate", savings.getInterestRate());
                            } else if (account instanceof CryptoAccount) {
                                CryptoAccount crypto = (CryptoAccount) account;
                                accountMap.put("walletAddress", crypto.getWalletAddress());
                            }

                            return accountMap;
                        })
                        .collect(Collectors.toList());
                response.put("accounts", accountsInfo);
            }

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
