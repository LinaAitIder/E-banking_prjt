package org.ebanking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.AccountRepository;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.UserConsentRepository;
import org.ebanking.model.Account;
import org.ebanking.model.Client;
import org.ebanking.model.UserConsent;
import org.springframework.beans.factory.annotation.Autowired;
import org.ebanking.model.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GDPRService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserConsentRepository consentRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Enregistre un consentement
    public UserConsent recordConsent(User user, String consentType, Duration validity) {
        UserConsent consent = new UserConsent(
                user,
                consentType,
                "CONSENT-" + UUID.randomUUID(),
                Instant.now().plus(validity)
        );
        return consentRepository.save(consent);
    }

    // Désactive un consentement (droit à l'oubli)
    public void revokeConsent(Long userId, String consentType) {
        consentRepository.findActiveByUserAndType(userId, consentType)
                .forEach(consent -> {
                    consent.setActive(false);
                    consentRepository.save(consent);
                });
    }

    // Exporte toutes les données utilisateur (JSON)
    public Resource exportClientData(Long clientId) {
        // 1. Récupération des données
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        List<UserConsent> consents = consentRepository.findByUserId(clientId);
        List<Account> accounts = accountRepository.findByOwnerId(clientId);

        // 2. Construction du JSON
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("client", Map.of(
                "id", client.getId(),
                "email", client.getEmail(),
                "phone", client.getPhone(),
                "createdAt", client.getCreatedAt()
        ));

        data.put("consents", consents.stream()
                .map(c -> Map.of(
                        "type", c.getConsentType(),
                        "givenAt", c.getGivenAt(),
                        "expiresAt", c.getExpiresAt()
                ))
                .collect(Collectors.toList()));

        data.put("accounts", accounts.stream()
                .map(a -> Map.of(
                        "accountNumber", a.getAccountNumber(),
                        "balance", a.getBalance(),
                        "type", a.getType()
                ))
                .collect(Collectors.toList()));

        // 3. Génération des fichiers
        try {
            // JSON
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(data);

            // PDF (simplifié)
            String pdfContent = "Données client #" + clientId + "\n\n" +
                    "Email: " + client.getEmail() + "\n" +
                    "Comptes: " + accounts.size() + "\n" +
                    "Consentements: " + consents.size();

            // Création d'un ZIP
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zipOut = new ZipOutputStream(baos);

            zipOut.putNextEntry(new ZipEntry("client-data.json"));
            zipOut.write(json.getBytes());
            zipOut.closeEntry();

            zipOut.putNextEntry(new ZipEntry("client-summary.pdf"));
            zipOut.write(pdfContent.getBytes());
            zipOut.closeEntry();

            zipOut.finish();

            return new ByteArrayResource(baos.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Export failed", e);
        }
    }
}
