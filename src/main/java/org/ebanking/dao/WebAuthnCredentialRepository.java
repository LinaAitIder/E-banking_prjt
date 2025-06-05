package org.ebanking.dao;

import org.ebanking.model.User;
import org.ebanking.model.WebAuthnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebAuthnCredentialRepository extends JpaRepository<WebAuthnCredential, Long> {

    boolean existsByUser(User user);

    @Query("SELECT w FROM WebAuthnCredential w WHERE w.user.id = :userId")
    List<WebAuthnCredential> findByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM WebAuthnCredential w WHERE w.credentialId = :credentialId")
    Optional<WebAuthnCredential> findByCredentialId(@Param("credentialId") String credentialId);
}