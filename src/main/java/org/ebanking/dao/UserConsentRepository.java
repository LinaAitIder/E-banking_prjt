package org.ebanking.dao;

import org.ebanking.model.UserConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConsentRepository extends JpaRepository<UserConsent, Long> {

    List<UserConsent> findByUserIdAndIsActiveTrue(Long userId);

    // Trouve tous les consentements actifs d'un utilisateur
    @Query("SELECT uc FROM UserConsent uc WHERE uc.user.id = :userId AND uc.isActive = true")
    List<UserConsent> findByUserId(@Param("userId") Long userId);

    // Trouve les consentements actifs par type
    @Query("SELECT uc FROM UserConsent uc WHERE uc.user.id = :userId AND uc.consentType = :consentType AND uc.isActive = true")
    List<UserConsent> findActiveByUserAndType(@Param("userId") Long userId,
                                              @Param("consentType") String consentType);
}
