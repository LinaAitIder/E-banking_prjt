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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Client client = authService.authenticate(request.getEmail(), request.getPassword());

        // seulement le role "ROLE_CLIENT" for now
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_CLIENT")
        );

        String token = jwtUtil.generateToken(client.getEmail(), authorities);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(new LoginResponse(token, client.isWebAuthnEnabled()));
    }
}