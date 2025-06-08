package org.ebanking.controller;

import org.ebanking.model.Client;
import org.ebanking.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/clients/unenrolled")
    public ResponseEntity<List<Client>> getUnenrolledClients() {
        List<Client> clients = agentService.getClientsWithoutEnrollment();
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/enroll/{clientId}")
    public ResponseEntity<Void> enrollClient(@PathVariable Long clientId,
                                             @RequestHeader("agent-id") Long agentId) {
        agentService.createEnrollment(agentId, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAgentClients(@RequestHeader("agent-id") Long agentId) {
        List<Client> clients = agentService.getAgentClients(agentId);
        return ResponseEntity.ok(clients);
    }
}
