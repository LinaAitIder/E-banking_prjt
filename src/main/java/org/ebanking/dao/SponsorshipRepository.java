package org.ebanking.dao;

import org.ebanking.model.Client;
import org.ebanking.model.Sponsorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Long> {

    Optional<Sponsorship> findByReferralCode(String referralCode);

    List<Sponsorship> findBySponsorAndStatus(Client sponsor, Sponsorship.SponsorshipStatus status);

    List<Sponsorship> findByReferredClient(Client referredClient);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Sponsorship s WHERE s.referralCode = :referralCode")
    boolean existsByReferralCode(@Param("referralCode") String referralCode);

    @Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :clientId")
    List<Sponsorship> findBySponsorId(@Param("clientId") Long clientId);

    @Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :clientId AND s.active = true")
    List<Sponsorship> findActiveSponsorshipsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(s) FROM Sponsorship s WHERE s.sponsor.id = :clientId AND s.status = 'COMPLETED'")
    long countCompletedSponsorships(@Param("clientId") Long clientId);
}
