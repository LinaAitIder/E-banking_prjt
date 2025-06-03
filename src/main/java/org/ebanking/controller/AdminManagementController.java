package org.ebanking.controller;

import org.ebanking.dao.UserRepository;
import org.ebanking.dto.request.AdminCreationRequest;
import org.ebanking.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> createAdminAccount(
            @RequestBody AdminCreationRequest request,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // Verifier que l'utilisateur a les droits .....

        // Création du compte admin
        Admin admin = new Admin();
        admin.setFullName(request.getFullName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhone(request.getPhone());
        admin.setDepartment(request.getDepartment());
        admin.setFirstLogin(true); // Premier login requis
        admin.setActive(true);

        userRepository.save(admin);

        return ResponseEntity.ok().body(Map.of(
                "message", "Admin account created successfully",
                "email", admin.getEmail(),
                "isFirstLogin", admin.isFirstLogin()
        ));
    }
}
