package org.ebanking.service;

import jakarta.persistence.EntityNotFoundException;
import org.ebanking.dao.ClientRepository;
import org.ebanking.dao.SponsorshipRepository;
import org.ebanking.model.Client;
import org.ebanking.model.Sponsorship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class SponsorshipService {


    @Autowired
    private SponsorshipRepository sponsorshipRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @Value("${sponsorship.bonus.sponsor:50.00}")
    private BigDecimal sponsorBonusAmount;

    @Value("${sponsorship.bonus.referred:25.00}")
    private BigDecimal referredClientBonusAmount;

    public Sponsorship createSponsorship(Long sponsorId, String referralCode) {
        Client sponsor = clientRepository.findById(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("Client sponsor not found"));

        if (sponsorshipRepository.existsByReferralCode(referralCode)) {
            throw new IllegalStateException("Referral code already exists");
        }

        Sponsorship sponsorship = new Sponsorship();
        sponsorship.setSponsor(sponsor);
        sponsorship.setReferralCode(referralCode);
        sponsorship.setStatus(Sponsorship.SponsorshipStatus.PENDING);

        return sponsorshipRepository.save(sponsorship);
    }

    public void applySponsorship(String referralCode, Long referredClientId) {
        // 1. Récupération des entités
        Sponsorship sponsorship = sponsorshipRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new EntityNotFoundException("Invalid referral code"));

        Client referredClient = clientRepository.findById(referredClientId)
                .orElseThrow(() -> new EntityNotFoundException("Referred client not found"));

        if (referredClient.getMainAccount() == null) {
            throw new IllegalStateException("Client has no active account");
        }

        // 2. Validation
        if (!sponsorship.isActive()) {
            throw new IllegalStateException("Sponsorship is not active");
        }
        if (sponsorship.getSponsor().getId().equals(referredClientId)) {
            throw new IllegalStateException("Cannot sponsor yourself");
        }

        // 3. Application du parrainage
        sponsorship.setReferredClient(referredClient);
        sponsorship.setStatus(Sponsorship.SponsorshipStatus.ACTIVE);

        // 4. Crédit des comptes
        accountService.creditAccount(
                sponsorship.getSponsor().getMainAccount(),
                sponsorBonusAmount
        );
        accountService.creditAccount(
                referredClient.getMainAccount(),
                referredClientBonusAmount
        );

        sponsorship.applyBonus(sponsorBonusAmount, referredClientBonusAmount);
        sponsorshipRepository.save(sponsorship);
    }

    public List<Sponsorship> getClientSponsorships(Long clientId) {
        return sponsorshipRepository.findBySponsorId(clientId);
    }
}
