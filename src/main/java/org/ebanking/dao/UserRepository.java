package org.ebanking.dao;

import org.ebanking.model.Admin;
import org.ebanking.model.Client;
import org.ebanking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.sql.RowSet;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.phone = :phone")
    boolean existsByPhone(@Param("phone") String phone);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c WHERE c.nationalId = :nationalId")
    boolean existsByNationalId(@Param("nationalId") String nationalId);

    // Nouvelle méthode pour vérifier l'existence d'un email
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Méthode pour trouver un admin par email
    @Query("SELECT a FROM Admin a WHERE a.email = :email")
    Optional<Admin> findAdminByEmail(@Param("email") String email);

    // Méthode existante à conserver
    @Query("SELECT c FROM Client c WHERE c.nationalId = :nationalId")
    Optional<Client> findByNationalId(@Param("nationalId") String nationalId);

    User findByPhone(String phoneNumber);


}
