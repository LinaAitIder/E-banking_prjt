package org.ebanking.controller;

import org.ebanking.model.Sponsorship;
import org.ebanking.service.SponsorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsorships")
public class SponsorshipController {

    @Autowired
    private SponsorshipService sponsorshipService;

    @PostMapping
    public ResponseEntity<Sponsorship> createSponsorship(
            @RequestParam Long sponsorId,
            @RequestParam String referralCode) {

        Sponsorship sponsorship = sponsorshipService.createSponsorship(sponsorId, referralCode);
        return ResponseEntity.ok(sponsorship);
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applySponsorship(
            @RequestParam String referralCode,
            @RequestParam Long referredClientId) {

        sponsorshipService.applySponsorship(referralCode, referredClientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Sponsorship>> getClientSponsorships(@PathVariable Long clientId) {
        return ResponseEntity.ok(sponsorshipService.getClientSponsorships(clientId));
    }
}
