package org.ebanking.controller;

import org.ebanking.model.Client;
import org.ebanking.security.webauthn.WebAuthnService;
import org.ebanking.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private WebAuthnService webAuthnService;

    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        // 1. Enregistrement basique
        Client registeredClient = registrationService.registerClient(client);

        // 2. Préparation pour WebAuthn
        String challenge = webAuthnService.prepareWebAuthnRegistration(registeredClient);

        return ResponseEntity.ok()
                .header("X-WebAuthn-Challenge", challenge)
                .body(registeredClient);
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verifyRegistration(
            @RequestParam String attestation,
            @RequestBody Client client) {

        webAuthnService.verifyRegistration(attestation, client);
        return ResponseEntity.ok().build();
    }
}