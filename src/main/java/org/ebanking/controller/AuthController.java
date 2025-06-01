package org.ebanking.controller;

import org.ebanking.dto.request.LoginRequest;
import org.ebanking.dto.response.LoginResponse;
import org.ebanking.model.Client;
import org.ebanking.dao.ClientRepository;
import org.ebanking.util.JwtUtil;
import org.ebanking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.ebanking.dao.WebAuthnCredentialRepository;
import org.ebanking.dto.request.BiometricAuthRequest;
import org.ebanking.dto.response.AuthResponse;
import org.ebanking.model.User;
import org.ebanking.security.webauthn.WebAuthnService;
import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final WebAuthnService webAuthnService;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final WebAuthnCredentialRepository credentialRepository;


    @Autowired
    public AuthController(AuthService authService, WebAuthnService webAuthnService,
                          JwtUtil jwtUtil, WebAuthnCredentialRepository credentialRepository){
        this.webAuthnService = webAuthnService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.credentialRepository = credentialRepository;
    }

    @PostMapping("/verification/password")
    public ResponseEntity<AuthResponse> verifyPassword(
            @RequestBody LoginRequest request,
            @RequestParam(required = false) String role) {

        // 1. Authentification basique
        User user = authService.authenticate(request.getEmail(), request.getPassword());

        // 2. Vérification du role si nécessaire
        if (role != null && !user.getRole().equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid role");
        }

        // 3. Préparation du challenge biométrique
        AuthResponse response = webAuthnService.prepareLoginChallenge(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verification/biometric")
    public ResponseEntity<LoginResponse> verifyBiometric(
            @RequestBody BiometricAuthRequest request) {

        User user = webAuthnService.verifyBiometricAuthentication(
                request.email(),
                request.credentialId(),
                request.authenticatorData(),
                request.clientDataJSON(),
                request.signature());

        // Conversion des roles en GrantedAuthority
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getEmail(), authorities);

        // Modification pour tous les types d'utilisateurs
        boolean webAuthnEnabled = credentialRepository.existsByUser(user);
        return ResponseEntity.ok(new LoginResponse(
                token,
                webAuthnEnabled,
                user.getRole() // Retourne "CLIENT", "AGENT" ou "ADMIN"
        ));
    }
}