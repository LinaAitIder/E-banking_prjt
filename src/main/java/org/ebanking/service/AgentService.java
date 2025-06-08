package org.ebanking.service;

import org.ebanking.dao.BankAgentRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.EnrollmentRepository;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.ebanking.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AgentService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BankAgentRepository bankAgentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Client> getClientsWithoutEnrollment() {
        return clientRepository.findByIsEnrolledFalse();
    }

    public void createEnrollment(Long agentId, Long clientId) {
        BankAgent agent = bankAgentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setAgent(agent);
        enrollment.setClient(client);
        enrollmentRepository.save(enrollment);

        client.setEnrolled(true);
        client.setResponsibleAgent(agent);
        clientRepository.save(client);
    }

    public List<Client> getAgentClients(Long agentId) {
        return enrollmentRepository.findByAgentId(agentId)
                .stream()
                .map(Enrollment::getClient)
                .collect(Collectors.toList());
    }

    // Autres methodes...
}
