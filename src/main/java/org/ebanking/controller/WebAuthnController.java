package org.ebanking.controller;

import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.ebanking.security.webauthn.WebAuthnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webauthn")
public class WebAuthnController {

    @Autowired
    private WebAuthnService webAuthnService;

    @PostMapping("/register/challenge")
    public String generateRegistrationChallenge(@RequestBody User user) {
        return webAuthnService.generateRegistrationOptions(user).toString();
    }

    @PostMapping("/register/verify")
    public void verifyRegistration(@RequestParam String attestation,
                                   @RequestBody Client client) {
        webAuthnService.verifyRegistration(attestation, client);
    }

    @PostMapping("/authenticate/challenge")
    public String generateAuthenticationChallenge(@RequestBody Client client) {
        return webAuthnService.generateAuthenticationOptions(client).toString();
    }

    @PostMapping("/authenticate/verify")
    public boolean verifyAuthentication(@RequestParam String assertion,
                                        @RequestBody Client client) {
        return webAuthnService.verifyAuthentication(assertion, client);
    }
}