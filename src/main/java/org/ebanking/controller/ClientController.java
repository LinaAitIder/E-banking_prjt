package org.ebanking.controller;

import org.ebanking.dao.ClientRepository;
import org.ebanking.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAllClients() {
        // Utilisez JOIN FETCH pour charger les relations nécessaires
        return clientRepository.findAllWithAgent();
    }
}


