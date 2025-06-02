package org.ebanking.controller;

import org.ebanking.dto.BankAgentRegistrationDTO;
import org.ebanking.dto.ClientRegistrationDTO;
import org.ebanking.model.BankAgent;
import org.ebanking.model.Client;
import org.ebanking.security.webauthn.WebAuthnService;
import org.ebanking.service.PhoneVerificationService;
import org.ebanking.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
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
//        if (!client.isPhoneVerified()) {
//            throw new IllegalStateException("Numéro non vérifié");
//        }
        // 1. Enregistrement basique
        Client registeredClient = registrationService.registerClient(client);

        // 2. Préparation pour WebAuthn
        String challenge = webAuthnService.prepareWebAuthnRegistration(registeredClient);



        System.out.println(registeredClient);
        return ResponseEntity.ok()
                .header("X-WebAuthn-Challenge", challenge)
                .body(registeredClient);
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verifyRegistration(
            @RequestParam("attestation") String attestation,
            @RequestBody ClientRegistrationDTO clientDto
    ) {
        Client client = new Client();
        client.setId(clientDto.getId());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setPassword(clientDto.getPassword());
        client.setNationalId(clientDto.getNationalId());
        client.setAddress(clientDto.getAddress());
        client.setCity(clientDto.getCity());
        client.setCountry(clientDto.getCountry());
        client.setDateOfBirth(clientDto.getDateOfBirth());
        client.setTermsAccepted(clientDto.getTermsAccepted());
        client.setChallenge(clientDto.getChallenge());
        webAuthnService.verifyRegistration(attestation, client);


        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/agent")
    public ResponseEntity<BankAgent> registerBankAgent(@RequestBody BankAgent agent) {
        // 1. Enregistrement basique
        BankAgent registeredAgent = registrationService.registerBankAgent(agent);

        // 2. Préparation pour WebAuthn
        String challenge = webAuthnService.prepareWebAuthnRegistration(registeredAgent);

        return ResponseEntity.ok()
                .header("X-WebAuthn-Challenge", challenge)
                .body(registeredAgent);
    }

    @PostMapping("/register/agent/verify")
    public ResponseEntity<?> verifyAgentRegistration(
            @RequestParam("attestation") String attestation,
            @RequestBody BankAgentRegistrationDTO agentDto) {

        BankAgent agent = new BankAgent();
        agent.setId(agentDto.getId());
        agent.setEmail(agentDto.getEmail());
        agent.setPhone(agentDto.getPhone());
        agent.setPassword(agentDto.getPassword());
        agent.setAgentCode(agentDto.getAgentCode());
        agent.setAgency(agentDto.getAgency());
        agent.setChallenge(agentDto.getChallenge());

        webAuthnService.verifyRegistration(attestation, agent);
        return ResponseEntity.ok().build();
    }
}