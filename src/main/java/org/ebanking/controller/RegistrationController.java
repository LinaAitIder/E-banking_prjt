package org.ebanking.controller;

import org.ebanking.model.Client;
import org.ebanking.security.webauthn.WebAuthnService;
import org.ebanking.service.PhoneVerificationService;
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
    private PhoneVerificationService phoneService;

    @Autowired
    private WebAuthnService webAuthnService;

    @PostMapping("/request-sms-verification")
    public ResponseEntity<?> requestSmsVerification(@RequestParam String phoneNumber) {
        if (registrationService.phoneNumberExists(phoneNumber)) {
            return ResponseEntity.badRequest().body("Numéro déjà utilisé");
        }
        phoneService.sendVerificationCode(phoneNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-sms")
    public ResponseEntity<?> verifySmsCode(
            @RequestParam String phoneNumber,
            @RequestParam String code) {
        if (phoneService.verifyCode(phoneNumber, code)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).body("Code SMS invalide");
    }

    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        // Verification que le SMS a ete valide
        if (!client.isPhoneVerified()) {
            throw new IllegalStateException("Numéro non vérifié");
        }
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