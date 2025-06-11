package org.ebanking.service;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.BankAgentRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.EnrollmentRepository;
import org.ebanking.dao.UserRepository;
import org.ebanking.model.*;
import org.hibernate.Hibernate;
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
    private UserRepository userRepository;

    @Autowired
    private BankAgentRepository bankAgentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Client> getUnenrolledClients() {
        List<Client> clients = clientRepository.findClientsWithoutEnrollment();
        System.out.println("Unenrolled clients found: " + clients.size());
        return clients;
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
        userRepository.save(client);
    }

    public List<Client> getAgentClients(Long agentId) {
        return enrollmentRepository.findByAgentId(agentId).stream()
                .map(Enrollment::getClient)
                .peek(client -> {
                    // Initialisation explicite
                    Hibernate.initialize(client.getAccounts());
                    if (client.getResponsibleAgent() != null) {
                        Hibernate.initialize(client.getResponsibleAgent());
                    }
                })
                .collect(Collectors.toList());
    }

    public Client getClientDetails(Long clientId) {
        return clientRepository.findById(clientId)
                .map(client -> {
                    // Charge les comptes avec leurs sous-types specifiques
                    Hibernate.initialize(client.getAccounts());
                    if (client.getAccounts() != null) {
                        client.getAccounts().forEach(account -> {
                            // Force le chargement du type specifique
                            if (account instanceof CurrentAccount) {
                                Hibernate.initialize(((CurrentAccount) account).getOverdraftLimit());
                            } else if (account instanceof SavingsAccount) {
                                Hibernate.initialize(((SavingsAccount) account).getInterestRate());
                            } else if (account instanceof CryptoAccount) {
                                Hibernate.initialize(((CryptoAccount) account).getWalletAddress());
                            }
                        });
                    }
                    if (client.getResponsibleAgent() != null) {
                        Hibernate.initialize(client.getResponsibleAgent());
                    }
                    return client;
                })
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }
}
